package org.rutiger.theatre.actors.exercise;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;

import java.util.Random;
import java.util.function.Supplier;

public abstract class Companion extends AbstractActor {

    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    protected Random r = new Random();
    protected Integer counter = 0;

    protected Integer attackWith(Integer max) { return r.nextInt(max) + 1; }

    protected Supplier<Integer> extraEffort = () -> (int)(System.currentTimeMillis() % 2);

    protected Receive commonBehavior() {
        return receiveBuilder()
                .match(Messages.Killed.class, killed -> {
                    counter += killed.orcs();
                })
                .match(Messages.EndBattle.class, any -> {
                    log.info("Hey i killed {} orcs", counter);
                    getContext().stop(getSelf());
                })
                .matchAny(o -> log.info("I dont understand my friend"))
                .build();
    }
}