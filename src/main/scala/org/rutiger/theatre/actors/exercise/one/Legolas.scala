package org.rutiger.theatre.actors.exercise.one

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.one.Legolas.Shot
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed}

class Legolas(friend: ActorRef) extends Actor with ActorLogging with Companion {

  private val MAX_ARROWS = 7

  override def receive: Receive = onDuty.orElse(commonBehavior)

  def onDuty: Receive = {
    case Attack => {
      log.info("Shooting arrows")
      val arrows = attackWith(MAX_ARROWS)
      sender() ! Shot(arrows)
      if (arrows > 5) {
        log.info("too tired after {} arrows", arrows)
        context.become(tired(3).orElse(commonBehavior))
      }
    }
  }

  def tired(waiting: Int): Receive = {
    case Attack => {
      log.info("Cannot do anything... protect me friend")
      friend forward Attack
      if (waiting < 1) {
        context.become(onDuty.orElse(commonBehavior))
        log.info("I am refreshed!!!")
      }
      else context.become(tired(waiting - 1).orElse(commonBehavior))
    }
  }

  override def postStop(): Unit = {
    log.info("Before the party, i gotta tell my pal")
    friend ! EndBattle
  }
}

object Legolas {
  def apply(friend: ActorRef): Props = Props(classOf[Legolas], friend)
  final case class Shot(arrows: Int)
}
