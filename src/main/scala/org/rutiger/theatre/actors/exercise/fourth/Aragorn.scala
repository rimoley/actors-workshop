package org.rutiger.theatre.actors.exercise.fourth

import akka.actor.{ActorRef, OneForOneStrategy, Props, SupervisorStrategy, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import org.rutiger.theatre.App.Attack
import org.rutiger.theatre.actors.exercise.Companion
import org.rutiger.theatre.actors.exercise.two.Battle.{EndBattle, Killed}

import scala.concurrent.duration._

class Aragorn(ghostSoldiers: Int) extends Companion {

  var armyCount: Int = 0

  def ghostName: String = {
    val names = Vector("Ernie", "Blaufas", "Pek", "Brotar", "De_la_Cuadra_Salcedo", "Kormak",
      "Chompi", "Jose_Luis", "Tortimer", "Truffles", "Poncho")
    val position: Int = armyCount % names.size
    armyCount+=1
    return s"${names(position)}$position"
  }

  var attackRouting = {
    val ghosts = Vector.fill(ghostSoldiers) {
      val r = context.actorOf(Props[DunharrowGhost], ghostName)
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), ghosts)
  }

  override def receive: Receive = onDuty.orElse(commonBehavior)

  private def onDuty: Receive = {
    case Attack => {
      attackRouting.route(Attack, sender())
    }
    case Killed(enemies) => {
      log.info("Ghost soldier {} informed me, {} enemies have been killed", sender().toString(), enemies)
      counter += enemies
    }
    case Terminated(ghost) => {
      attackRouting = attackRouting.removeRoutee(ghost)
      val summonedGhost: ActorRef = context.actorOf(Props[DunharrowGhost], ghostName)
      context watch summonedGhost
      attackRouting = attackRouting.addRoutee(summonedGhost)
      log.info("I needed to summon a new Ghost!!")
    }
    case EndBattle => {
      log.info("Hey I killed {}", counter)
      log.info("Sending away my remaining ghost soldiers...")
      context.children.foreach( child => {
        context.unwatch(child)
        attackRouting.removeRoutee(child)
        log.info("Your task is fullfilled {}", child)
        context.stop(child)
      })
      context.stop(self)
    }
    case _ => log.warning("I am scared")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5 seconds) {
    case _ => {
      log.info("Restarting actor")
      SupervisorStrategy.Restart
    }
  }
}

object Aragorn {
  def apply(ghostSoldiers: Int): Props = Props(new Aragorn(ghostSoldiers))
}