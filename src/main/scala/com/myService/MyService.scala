package com.myService

import akka.actor.Actor
import com.myService.logic.{OwnerLogic, PetLogic}
import com.myService.models.{Owner, Pet}
import spray.http.MediaTypes._
import spray.routing._

import scala.util.{Failure, Success}

class MyServiceActor extends Actor with MyService {
  def actorRefFactory = context

  def receive = runRoute(myRoute)
}

trait MyService extends HttpService {

  import com.myService.models.OwnerJsonProtocol._
  import com.myService.models.PetJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  private implicit def ec = actorRefFactory.dispatcher

  val petLogic = new PetLogic
  val ownerLogic = new OwnerLogic

  val myRoute =
    path("pet" / IntNumber) { id =>
      get {
        onComplete(petLogic.getPet(id)) {
          case Success(value) => complete(value)
          case Failure(ex) => complete("Uh oh!")
        }
      } ~
      put {
        entity(as[Pet]) { pet =>
          onComplete(petLogic.updateOrRegisterPet(pet, id)) {
            case Success(value) => complete("Success!")
            case Failure(ex) => complete("Uh oh!")
          }
        }
      } ~
      delete {
        onComplete(petLogic.deletePet(id)) {
          case Success(value) => complete("Success!")
          case Failure(ex) => complete("Uh oh")
        }
      }
    } ~
    path("pets") {
      get {
        respondWithMediaType(`application/json`) {
          parameters("name"?) { (name) =>
            //todo
            onComplete(petLogic.getPets(name)) {
              case Success(value) => {
                import com.myService.models.PetJsonProtocol._
                import spray.httpx.SprayJsonSupport._
                complete(value)
              }
              case Failure(ex) => complete("Uh Oh")
            }
          }
        }
      } ~
      post {
        entity(as[Pet]) { pet =>
          onComplete(petLogic.updateOrRegisterPet(pet)) {
            case Success(value) => complete("Success!")
            case Failure(ex) => complete("Uh oh")
          }
        }
      }
    } ~
    path("owner" / IntNumber) { petId =>
      put {
        entity(as[Owner]) { owner =>
          onComplete(ownerLogic.addOwner(petId, owner)) {
            case Success(value) => complete("Success!")
            case Failure(ex) => complete("Uh oh")
          }
        }
      }
    }
}