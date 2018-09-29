package org.rutiger.theatre.actors.exercise.two

import akka.actor.{Actor, ActorLogging, ActorRef}

import scala.concurrent.duration._
import scala.util.Random

class Battle(initialEnemyArmy: Int, warriors: List[ActorRef]) extends Actor with ActorLogging {

  private var enemyArmy: Int = initialEnemyArmy
  private val enemysDen: Random = Random

  /*
  TODO
  1) Send attack message to the warrios
  2) If a response from a warrior arrives, keep the proper count, and reply the warrior
  3) If there are no more enemies, end battle
   */
  override def receive: Receive = ???

  private def killOrcs(warrior: ActorRef, quantity: Int) = ???

  //TODO Add either a implicit dispatcher or add the dispatcher to the schedulers call

  //TODO create scheduler for enemies respawn
  val enemiesScheduler = ???

  //TODO create scheduler for ordering the attacks
  val attackOrdering = ???
}

object Battle {
  //TODO Create a props contructor
  def apply = ???

  final case class Attack()
  final case class Killed(enemies: Int)
  final case class ReinforcementsArrive(orcs: Int)
  object EndBattle
}