package org.rutiger.theatre.actors.exercise.one;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

/*
This class represents a guy that can shots 7 arrows at once. Epicness level over 9k
 */
public class Legolas extends AbstractActor implements Companion {
    private static Integer RESTING_TURNS = 3;
    private static Integer MAX_ARROWS = 7;
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Integer counter = 0;
    private ActorRef friend;

    private Legolas(ActorRef friend) {
        this.friend = friend;
    }

    @Override
    public Receive createReceive() {
        return onDuty().orElse(commonBehavior());
    }

    public Receive onDuty() {
        return receiveBuilder()
                .match(Messages.Attack.class, msg -> {
                    int arrowsShot = attackWith(MAX_ARROWS);
                    log.info("shot {} arrows", arrowsShot);
                    getSender().tell(new Messages.Shot(arrowsShot), getSelf());
                    if (arrowsShot > 5) {
                        log.info("too tired after {} arrows", arrowsShot);
                        getContext().become(tired(RESTING_TURNS).orElse(commonBehavior()));
                    }
                })
                .build();
    }

    private Receive tired(Integer waiting) {
        return receiveBuilder()
                .match(Messages.Attack.class, attack -> {
                    log.info("Cannot do anything... protect me friend");
                    friend.forward(attack, getContext());
                    if (waiting < 1) {
                        getContext().become(onDuty().orElse(commonBehavior()));
                        log.info("Feeling refreshed!!");
                    }
                    else {
                        getContext().become(tired(waiting - 1).orElse(commonBehavior()));
                    }
                })
                .build();
    }

    private Receive commonBehavior() {
        return receiveBuilder()
                .match(Messages.Killed.class, killed -> {
                    counter += killed.orcs();
                })
                .match(Messages.EndBattle.class, any -> {
                    log.info("Hey i killed {} orcs", counter);
                    getContext().stop(getSelf());
                })
                .build();
    }

    public static Props props(ActorRef friend) {
        return Props.create(Legolas.class, friend);
    }
}