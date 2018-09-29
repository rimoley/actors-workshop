package org.rutiger.theatre.actors.exercise.one

import akka.actor.{Actor, ActorLogging}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Gimli.Swung


class Gimli extends Actor with ActorLogging with Companion {
  private val MAX_SWINGING = 3

  override def receive: Receive = {
    case Attack => {
      log.info("Swinging my axe")
      sender() ! Swung(attackWith(MAX_SWINGING))
    }
    case _ => {
      log.warning("Me dont understand")
    }
  }
}

object Gimli {
  final case class Swung(axe: Int)
}

