package org.rutiger.theatre.actors

import akka.actor.Actor

/*
This class represents a guy that can shots 5 arrows at once. Epicness level over 9k
 */
class Legolas extends Actor{

  /*
  1) Create couple of message that the actor will receive
  2) Modify receive function to treat those messages and print some message
  3) Create Gimli Actor, with a couple of messages
  4) Legolas and Gimli should know each other? Select to use constructor or message block
   */
  override def receive: Receive = ???
}

object Legolas {

  final case class Attack()
  final case class Shot(arrows: Int)

}
