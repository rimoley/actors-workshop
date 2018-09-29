package org.rutiger.theatre.actors.exercise.one

import akka.actor.{ActorRef, Props}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Gimli.Swung

class Gimli(friend: ActorRef) extends Companion {
  private val MAX_SWINGING = 3

  override def receive: Receive = onDuty.orElse(commonBehavior)

  def onDuty: Receive = {
    case Attack => {
      log.info("Swinging my axe")
      val currentAttack = attackWith(MAX_SWINGING) + extraEffort()
      sender() ! Swung(currentAttack)
      if (currentAttack > 3) {
        log.info("too dizzy after {} swings", currentAttack)
        context.become(tired)
      }
    }
  }

  def tired: Receive = {
    case Attack => {
      log.info("Cannot do anything... protect me friend")
      friend forward Attack
      context.become(onDuty.orElse(commonBehavior))
      log.info("I am back!!!")
    }
  }
}

object Gimli {
  final case class Swung(axe: Int)
  def apply(friend: ActorRef): Props = Props(classOf[Gimli], friend)
}

