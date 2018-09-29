package org.rutiger.theatre.actors.exercise.third

import akka.actor.{Actor, ActorLogging, DeadLetter, Props}

class BattleScribe extends Actor with ActorLogging {

  //TODO assign some event stream
  val eventStream = ???

  //TODO map Deadletter messages
  def receive = ???

  //TODO hook the actor to listen to Deadletter messages
  override def preStart(): Unit = ???

  //TODO unhook it
  override def postStop(): Unit = ???
}


object BattleScribe {
  def apply: Props = Props[BattleScribe]
}
