package org.rutiger.theatre

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import org.rutiger.theatre.actors.exercise.one.{Gimli, Legolas}
import org.rutiger.theatre.actors.exercise.one.Legolas.Shot

object App {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("workshop")
    val legolas: ActorRef = actorSystem.actorOf(Props[Legolas])
    val gimli: ActorRef = actorSystem.actorOf(Props[Gimli])
    implicit val timeout: Timeout = Timeout(3 seconds)
    gimli ? Attack

    legolas ! Attack
    gimli ! Attack
    gimli ! Shot(1)

    legolas ! PoisonPill
    gimli ! PoisonPill
    
    actorSystem.terminate()
  }


  final case class Attack()
}
