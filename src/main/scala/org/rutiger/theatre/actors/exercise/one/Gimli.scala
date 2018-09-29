package org.rutiger.theatre.actors.exercise.one

import akka.actor.{Actor, ActorLogging, Props}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Gimli.Swung
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed}

class Gimli extends Actor with ActorLogging with Companion {
  private val MAX_SWINGING = 3

  private var counter = 0

  override def receive: Receive = {
    case Attack => {
      log.info("Swinging my axe")
      sender() ! Swung(attackWith(MAX_SWINGING))
    }
    case Killed(orcs) => counter += orcs
    case EndBattle => {
      log.info("Hey I killed {}", counter)
      context.stop(self)
    }
    case _ => {
      log.warning("Me dont understand")
    }
  }
}

object Gimli {
  final case class Swung(axe: Int)
  def apply: Props = Props[Gimli]
}

