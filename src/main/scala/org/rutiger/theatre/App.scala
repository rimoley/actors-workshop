package org.rutiger.theatre

import akka.actor.{ActorRef, ActorSystem}
import org.rutiger.theatre.actors.exercise.one.{Gimli, Legolas}
import org.rutiger.theatre.actors.exercise.third.BattleScribe
import org.rutiger.theatre.actors.exercise.two.Battle

object App {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("workshop")
    val gimli: ActorRef = actorSystem.actorOf(Gimli.apply, "Gimli")
    val legolas: ActorRef = actorSystem.actorOf(Legolas(gimli), "Legolas")
    val scribe: ActorRef = actorSystem.actorOf(BattleScribe.apply, "scribe")
    val battle: ActorRef = actorSystem.actorOf(Battle(100, List(legolas, gimli)))
  }


  final case class Attack()
}
