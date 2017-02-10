package com.myService.logic

import com.myService.db.{PetRecord, PetRepo}
import com.myService.models.Pet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PetLogic {

  import slick.driver.H2Driver.api._

  val repo = new PetRepo
  val ownerLogic = new OwnerLogic


  def getPets(name: Option[String]): Future[List[Pet]] = {
    repo.db.run(
      repo.pets
        .filter(p =>
          name.map { n =>
            p.name.toLowerCase === n.toLowerCase
          }.getOrElse(
            slick.lifted.LiteralColumn(true)
          )
        ).result
    ).map(_.map(_.toPet).toList)
  }


  def getPet(id: Int): Future[Option[Pet]] = {
    repo.db.run(
      repo.pets.filter(_.id === id).result
    ).map(_.map(_.toPet).headOption)
  }

  def updateOrRegisterPet(pet: Pet, id: Int = -1): Future[Unit] = {
    repo.db.run(
      repo.pets.insertOrUpdate(
        PetRecord(
          id,
          pet.name.value,
          pet.dob.value.toString,
          pet.notes
        )
      )
    ).map(_ => ())
  }

  def deletePet(id: Int): Future[Unit] = {
    for {
      _ <- ownerLogic.deleteOwner(id)
      _ <- repo.db.run(repo.pets.filter(_.id === id).delete)
    }
      yield ()
  }
}
