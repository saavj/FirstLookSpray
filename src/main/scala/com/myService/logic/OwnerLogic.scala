package com.myService.logic

import com.myService.db.{OwnerRecord, OwnerRepo}
import com.myService.models.Owner

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OwnerLogic {

  import slick.driver.H2Driver.api._

  val repo = new OwnerRepo

  def addOwner(petId: Int, owner: Owner): Future[Unit] = {
    repo.db.run(
      repo.owners += OwnerRecord(
        -1,
        petId,
        owner.name.firstName,
        owner.name.lastName,
        owner.address.firstLine,
        owner.address.town,
        owner.address.postcode,
        owner.phoneNumber.value
      )
    ).map(_ => ())
  }

  def updateOwner(petId: Int, owner: Owner): Future[Unit] = {
    repo.db.run(
      repo.owners.filter(_.petId === petId)
        .map(x => (x.firstName, x.lastName, x.firstLine, x.town, x.postcode, x.phoneNumber))
        .update(
          (owner.name.firstName,
            owner.name.lastName,
            owner.address.firstLine,
            owner.address.town,
            owner.address.postcode,
            owner.phoneNumber.value)
        )
    ).map(_ => ())
  }

  def deleteOwner(petId: Int): Future[Unit] = {
    repo.db.run(
      repo.owners.filter(_.petId === petId).delete
    ).map(_ => ())
  }
}
