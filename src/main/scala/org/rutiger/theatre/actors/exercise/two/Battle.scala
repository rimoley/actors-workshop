package org.rutiger.theatre.actors.exercise.two
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.fourth.DunharrowGhost.GhostSword
import org.rutiger.theatre.actors.exercise.one.Gimli.Swung
import org.rutiger.theatre.actors.exercise.one.Legolas.Shot
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed, ReinforcementsArrive}

import scala.concurrent.duration._
import scala.util.Random

class Battle(initialEnemyArmy: Int, warriors: List[ActorRef]) extends Actor with ActorLogging with Companion {

  private var enemyArmy: Int = initialEnemyArmy
  private val enemysDen: Random = Random

  override def receive: Receive = {
    case ReinforcementsArrive(someOrcs) => {
      log.info("{} orcs join the battle", someOrcs)
      enemyArmy += someOrcs
    }
    case Shot(arrows) => killOrcs(sender(), arrows)
    case Swung(axe) => killOrcs(sender(), axe)
    case GhostSword(slashes) => killOrcs(sender(), slashes)
    case Attack => Random.shuffle(warriors).foreach( _ ! Attack)
    case EndBattle => {
      warriors.foreach(_ ! EndBattle)
      context.stop(self)
    }
  }

  private def killOrcs(warrior: ActorRef, quantity: Int) = {
    log.info("{} kills as much as {} orcs", warrior.toString(), quantity)
    if (quantity < enemyArmy) warrior ! Killed(quantity)
    else {
      warrior ! Killed(enemyArmy)
      self ! EndBattle
    }
    enemyArmy -= quantity
  }

  private implicit val enemiesDispatcher = context.system.dispatcher

  val enemiesScheduler = {
    log.info("Enemies starts to arrive to battle")
    context.system.scheduler.schedule(
      5 seconds,
      5 seconds,
      self,
      ReinforcementsArrive(enemysDen.nextInt(10) + 1 ))
  }

  val attackOrdering = {
    log.info("Attacking the enemies")
    context.system.scheduler.schedule(
      1 seconds,
      1 seconds,
      self,
      Attack)
  }

  override def postStop(): Unit = {
    enemiesScheduler.cancel()
    attackOrdering.cancel()
    context.system.terminate()
 }
}

object Battle {
  def apply(initialEnemyArmy: Int, warriors: List[ActorRef]): Props = Props(new Battle(initialEnemyArmy, warriors))

  final case class Killed(enemies: Int)
  final case class ReinforcementsArrive(orcs: Int)
  object EndBattle
}