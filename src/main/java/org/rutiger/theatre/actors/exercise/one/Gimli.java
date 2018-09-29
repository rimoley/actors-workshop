package org.rutiger.theatre.actors.exercise.one;

import akka.actor.ActorRef;
import akka.actor.Props;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

public class Gimli extends Companion {

    private static final Integer MAX_SWINGING = 3;
    private ActorRef friend;

    private Gimli(ActorRef friend) {
        this.friend = friend;
    }

    @Override
    public Receive createReceive() {
        return onDuty().orElse(commonBehavior());
    }

    private Receive onDuty() {
        return receiveBuilder()
                .match(Messages.Attack.class, msg -> {
                    int axeSwung= attackWith(MAX_SWINGING) + extraEffort.get();
                    log.info("swung axe {} times", axeSwung);
                    getSender().tell(new Messages.Shot(axeSwung), getSelf());
                    if (axeSwung > MAX_SWINGING) {
                        log.info("too dizzy after {} swings", axeSwung);
                        getContext().become(tired().orElse(commonBehavior()));
                    }
                })
                .build();
    }

    private Receive tired() {
        return receiveBuilder()
                .match(Messages.Attack.class, attack -> {
                    log.info("Cannot do anything... protect me friend");
                    friend.forward(attack, getContext());
                    getContext().become(onDuty().orElse(commonBehavior()));
                    log.info("I am back!!");
                })
                .build();
    }

    public static Props props(ActorRef friend) {
        return Props.create(Gimli.class, friend);
    }
}
