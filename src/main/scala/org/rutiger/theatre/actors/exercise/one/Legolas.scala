package org.rutiger.theatre.actors.exercise.one

import akka.actor.{Actor, ActorLogging}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Legolas.Shot

class Legolas extends Actor with ActorLogging with Companion {

  private val MAX_ARROWS = 5

  override def receive: Receive = {
    case Attack =>
      log.info("Shooting arrows")
      sender() ! Shot(attackWith(MAX_ARROWS))
  }
}

object Legolas {

  final case class Shot(arrows: Int)

}
