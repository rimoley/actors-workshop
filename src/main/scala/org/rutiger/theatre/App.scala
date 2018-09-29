package org.rutiger.theatre

import akka.actor.{ActorRef, ActorSystem}
import org.rutiger.theatre.actors.exercise.one.{Gimli, Legolas}
import org.rutiger.theatre.actors.exercise.two.Battle

object App {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("workshop")
    val legolas: ActorRef = actorSystem.actorOf(Legolas.apply, "Legolas")
    val gimli: ActorRef = actorSystem.actorOf(Gimli.apply, "Gimli")

    val battle: ActorRef = actorSystem.actorOf(Battle(100, List(legolas, gimli)))
  }


  final case class Attack()
}
