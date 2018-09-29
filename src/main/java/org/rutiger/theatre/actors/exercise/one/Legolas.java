package org.rutiger.theatre.actors.exercise.one;

import akka.actor.ActorRef;
import akka.actor.Props;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

/*
This class represents a guy that can shots 7 arrows at once. Epicness level over 9k
 */
public class Legolas extends Companion {
    private static Integer RESTING_TURNS = 2;
    private static Integer MAX_ARROWS = 7;
    private ActorRef friend;

    private Legolas(ActorRef friend) {
        this.friend = friend;
    }

    @Override
    public Receive createReceive() {
        return onDuty().orElse(commonBehavior());
    }

    private Receive onDuty() {
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

    @Override
    public void postStop() throws Exception {
        log.info("Before the party, I gotta tell my pal");
        friend.tell(new Messages.EndBattle(), getSelf());
    }

    public static Props props(ActorRef friend) {
        return Props.create(Legolas.class, friend);
    }
}