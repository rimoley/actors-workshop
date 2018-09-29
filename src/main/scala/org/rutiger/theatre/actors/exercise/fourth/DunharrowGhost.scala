package org.rutiger.theatre.actors.exercise.fourth

import akka.actor.{Actor, ActorLogging, PoisonPill}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.fourth.DunharrowGhost.GhostSword

import scala.util.Random

class DunharrowGhost extends Actor with ActorLogging {

  private val MAX_SLASHES: Int = 2
  private val r = Random

  override def receive: Receive = {
    case Attack => {
      now % 2 == 0 match {
        case true => sender() ! GhostSword(r.nextInt(MAX_SLASHES) + 1)
        case false => mysteryErrorBox()
      }
    }
    case _ => log.info("bliba bluba blerg!!!! (What are you saying {})", sender().toString())
  }

  def mysteryErrorBox() = {
    now % 3 match {
      case 0 => throw new IllegalStateException("Ghost overflow")
      case 1 => self ! PoisonPill
      case 2 => context.stop(self)
    }
  }

  def now = System.currentTimeMillis()
}

object DunharrowGhost {
  final case class GhostSword(slashes: Int)
}