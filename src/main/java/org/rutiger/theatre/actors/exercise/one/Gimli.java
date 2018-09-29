package org.rutiger.theatre.actors.exercise.one;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

public class Gimli extends AbstractActor implements Companion {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Integer counter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, s -> {
                    int axeSwung= attackWith(3);
                    log.info("swung axe {} times", axeSwung);
                    getSender().tell(new Messages.Shot(axeSwung), getSelf());
                })
                .match(Messages.Killed.class, killed -> {
                    counter += killed.orcs();
                })
                .match(Messages.EndBattle.class, any -> {
                    log.info("Hey i killed {} orcs", counter);
                    getContext().stop(getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    public static Props props() {
        return Props.create(Gimli.class);
    }
}
