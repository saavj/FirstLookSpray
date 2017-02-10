package com.myService.models

import spray.json.DefaultJsonProtocol

case class OwnerName(firstName: String, lastName: String)

case class Address(firstLine: String, town: String, postcode: String)

case class PhoneNumber(value: String)

case class Owner(name: OwnerName, address: Address, phoneNumber: PhoneNumber)

object OwnerJsonProtocol extends DefaultJsonProtocol {
  implicit val name = jsonFormat2(OwnerName)
  implicit val address = jsonFormat3(Address)
  implicit val number = jsonFormat1(PhoneNumber)

  implicit val ownerFormat = jsonFormat3(Owner)
}