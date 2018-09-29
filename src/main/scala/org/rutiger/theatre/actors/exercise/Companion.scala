package org.rutiger.theatre.actors.exercise

import akka.actor.{Actor, ActorLogging}
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed}

import scala.util.Random

trait Companion extends Actor with ActorLogging {

  protected var counter = 0
  private val r: Random = Random
  val attackWith: Int => Int = max => r.nextInt(max) + 1
  val extraEffort: () => Int = () => (System.currentTimeMillis() % 2).toInt

  def commonBehavior: Receive = {
    case Killed(orcs) => counter += orcs
    case EndBattle => {
      log.info("Hey I killed {}", counter)
      context.stop(self)
    }
    case _ => log.warning("I dont understand my friend")
  }
}
