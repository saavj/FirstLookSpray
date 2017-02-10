package com.myService.db

import com.myService.models._
import slick.driver.H2Driver.api._
import slick.lifted.Tag

case class PetRecord(id: Int, name: String, dob: String, notes: String) {
  def toPet: Pet = {
    Pet(Some(id), Name(name), DOB(dob), notes)
  }
}

class PetRepo {

  val pets = TableQuery[PetTable]
  val db = Database.forConfig("h2mem1")
  val setup = DBIO.seq(pets.schema.create)
  val setupFuture = db.run(setup)

  class PetTable(tag: Tag) extends Table[PetRecord](tag, "pets") {

    def * = (id, name, dob, notes) <> ((PetRecord.apply _).tupled, PetRecord.unapply)

    def id = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def name = column[String]("name")

    def dob = column[String]("date_of_birth")

    def notes = column[String]("notes")
  }

}