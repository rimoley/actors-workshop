package org.rutiger.theatre.actors.exercise.one

import akka.actor.Actor

/*
This class represents a guy that can shots 5 arrows at once. Epicness level over 9k
 */
class Legolas extends Actor{

  //TODO Receive Attack message and send a randome number of arrows, tops to 5.
  override def receive: Receive = ???
}

object Legolas {

  final case class Attack()
  final case class Shot(arrows: Int)

}
