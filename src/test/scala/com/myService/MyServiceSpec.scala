package com.myService

import org.specs2.mutable.Specification
import spray.http.HttpEntity
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest
// imports the object with test data:
import spray.http.HttpRequest


class MyServiceSpec extends Specification with Specs2RouteTest with MyService {
  def actorRefFactory = system

  "MyService" should {

    "GET requests should return a list of pets" in {
      Get("/pets") ~> myRoute ~> check {
        response.status should be equalTo OK
      }
    }

    "POST requests with valid payloads should return a 'Success!' for " in {
      HttpRequest(
        PUT, s"/pets",
        entity = HttpEntity(
          "" //todo
        )
      ) ~> myRoute ~> check {
        response.status should be equalTo OK
        response.entity should not be equalTo(None)
        responseAs[String] must be equalTo ""
      }
    }
  }
}
