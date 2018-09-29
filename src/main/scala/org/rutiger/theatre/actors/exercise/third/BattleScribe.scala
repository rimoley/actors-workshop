package org.rutiger.theatre.actors.exercise.third

import akka.actor.{Actor, ActorLogging, DeadLetter, Props}
import akka.event.Logging.Info

class BattleScribe extends Actor with ActorLogging {

  val eventStream = context.system.eventStream

  def receive = {
    case DeadLetter(message, sender, receiver) => {
      val msg = s"Cannot deliver message [${message.getClass.getName}] from $sender to $receiver"
      log.warning("BATTLE LOG => {}", msg)
      eventStream.publish(Info(receiver.path.toString, receiver.getClass, msg))
    }
  }

  override def preStart(): Unit = eventStream.subscribe(self, classOf[DeadLetter])

  override def postStop(): Unit = eventStream.unsubscribe(self)
}


object BattleScribe {
  def apply: Props = Props[BattleScribe]
}
