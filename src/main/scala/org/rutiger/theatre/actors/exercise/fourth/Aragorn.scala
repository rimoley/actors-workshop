package org.rutiger.theatre.actors.exercise.fourth

import akka.actor.{Actor, ActorLogging}

/*TODO
Create new warrior, Aragorn. It is an actor router with 3 children
Create a Router an initialize it with RoundRobin policy for balancing
Use this actor for create 3 children actor of the new "Warrior"
Use futureAsk pattern
Set up a supervision strategy with your preference in what to do in each case
Battle should respond to the Aragorn actor to keep track
Adapt rest of actor accordingly
 */
class Aragorn extends Actor with ActorLogging {

  override def receive: Receive = ???
}
