package org.rutiger.theatre.actors.exercise.third;

import akka.actor.AbstractActor;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import org.rutiger.theatre.Messages;


public class BattleScribe extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    //Receive a dead letter message and process it.
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeadLetter.class, dlm -> {
                    String message = String.format("Cannot deliver message '%s' from %s to %s",
                            dlm.getClass().getName(), dlm.sender().path(), dlm.recipient().path());
                    log.info("Battle LOG: {}", message);
                    getContext().getSystem().eventStream().publish(message);
                }).build();
    }

    @Override
    public void preStart() throws Exception {
        getContext().getSystem().eventStream().subscribe(getSelf(), DeadLetter.class);
    }

    @Override
    public void postStop() throws Exception {
        getContext().getSystem().eventStream().unsubscribe(getSelf());
    }

    public static Props props() {
        return Props.create(BattleScribe.class);
    }
}
