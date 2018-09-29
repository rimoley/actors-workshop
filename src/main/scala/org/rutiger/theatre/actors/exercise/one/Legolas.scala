package org.rutiger.theatre.actors.exercise.one

import akka.actor.{Actor, ActorLogging, Props}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Legolas.Shot
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed}

class Legolas extends Actor with ActorLogging with Companion {

  private val MAX_ARROWS = 5
  private var counter = 0

  override def receive: Receive = {
    case Attack => {
      log.info("Shooting arrows")
      sender() ! Shot(attackWith(MAX_ARROWS))
    }
    case Killed(orcs) => counter += orcs
    case EndBattle => {
      log.info("Hey I killed {}", counter)
      context.stop(self)
    }
  }
}

object Legolas {
  def apply: Props = Props[Legolas]
  final case class Shot(arrows: Int)

}
