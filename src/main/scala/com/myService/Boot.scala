package com.myService

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("myActor")

  val service = system.actorOf(Props[MyServiceActor], "myService")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 9000)
}
