package org.rutiger.theatre.actors.exercise.one;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

/*
This class represents a guy that can shots 5 arrows at once. Epicness level over 9k
 */
public class Legolas extends AbstractActor implements Companion {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Integer counter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, msg -> {
                    int arrowsShot = attackWith(5);
                    log.info("shot {} arrows", arrowsShot);
                    getSender().tell(new Messages.Shot(arrowsShot), getSelf());
                })
                .match(Messages.Killed.class, killed -> {
                    counter += killed.orcs();
                })
                .match(Messages.EndBattle.class, any -> {
                    log.info("Hey i killed {} orcs", counter);
                    getContext().stop(getSelf());
                })
                .build();
    }

    public static Props props() {
        return Props.create(Legolas.class);
    }
}