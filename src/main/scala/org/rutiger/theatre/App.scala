package org.rutiger.theatre

import akka.actor.{ActorRef, ActorSystem}
import org.rutiger.theatre.actors.exercise.fourth.Aragorn
import org.rutiger.theatre.actors.exercise.one.{Gimli, Legolas}
import org.rutiger.theatre.actors.exercise.third.BattleScribe
import org.rutiger.theatre.actors.exercise.two.Battle

import scala.concurrent.Await
import scala.concurrent.duration._

object App {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("workshop")
    val aragorn = actorSystem.actorOf(Aragorn(3), "Aragorn")
    val gimli: ActorRef = actorSystem.actorOf(Gimli(aragorn), "Gimli")
    val legolas: ActorRef = actorSystem.actorOf(Legolas(aragorn), "Legolas")
    val scribe: ActorRef = actorSystem.actorOf(BattleScribe.apply, "scribe")
    val battle: ActorRef = actorSystem.actorOf(Battle(100, List(legolas, gimli)))

    Await.result(actorSystem.whenTerminated, 60 seconds)

  }


  final case class Attack()
}
