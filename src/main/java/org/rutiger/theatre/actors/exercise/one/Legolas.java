package org.rutiger.theatre.actors.exercise.one;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;

/*
This class represents a guy that can shots 5 arrows at once. Epicness level over 9k
 */
public class Legolas extends AbstractActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /*
        1) Create couple of message that the actor will receive
        2) Modify receive function to treat those messages and print some message
        3) Create Gimli Actor, with a couple of messages
        4) Legolas and Gimli should know each other? Select to use constructor or message block
     */

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, s -> {
                    log.info("shot {} arrows", s);
                    getSender().tell(new Messages.Shot(3), getSelf());
                })
                .build();
    }
}
