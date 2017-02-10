package com.myService.models

import spray.json.DefaultJsonProtocol

case class Name(value: String)

case class DOB(value: String)

case class Pet(id: Option[Int], name: Name, dob: DOB, notes: String)

object PetJsonProtocol extends DefaultJsonProtocol {
  implicit val nameFormat = jsonFormat1(Name)
  implicit val dobFormat = jsonFormat1(DOB)

  implicit val petFormat = jsonFormat4(Pet)
}
