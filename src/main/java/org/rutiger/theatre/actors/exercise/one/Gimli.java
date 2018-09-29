package org.rutiger.theatre.actors.exercise.one;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

public class Gimli extends AbstractActor implements Companion {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, s -> {
                    int axeSwung= attackWith(3);
                    log.info("swung axe {} times", axeSwung);
                    getSender().tell(new Messages.Swung(axeSwung), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
