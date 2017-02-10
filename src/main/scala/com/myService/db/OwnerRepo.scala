package com.myService.db

import slick.driver.H2Driver.api._
import slick.lifted.Tag

case class OwnerRecord(
                        id: Int,
                        petId: Int,
                        firstName: String,
                        lastName: String,
                        firstLine: String,
                        town: String,
                        postcode: String,
                        phoneNumber: String)


class OwnerRepo {

  val db = Database.forConfig("h2mem1")
  val owners = TableQuery[OwnerTable]
  val setup = DBIO.seq(owners.schema.create)
  val setupFuture = db.run(setup)

  class OwnerTable(tag: Tag) extends Table[OwnerRecord](tag, "owners") {

    def * = (id, petId, firstName, lastName, firstLine, town, postcode, phoneNumber) <> ((OwnerRecord.apply _).tupled, OwnerRecord.unapply)

    def id = column[Int]("id", O.AutoInc, O.PrimaryKey)

    def petId = column[Int]("pet_id")

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def firstLine = column[String]("first_line")

    def town = column[String]("town")

    def postcode = column[String]("postcode")

    def phoneNumber = column[String]("phone_number")
  }

}