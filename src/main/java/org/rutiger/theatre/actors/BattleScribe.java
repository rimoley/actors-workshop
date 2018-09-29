package org.rutiger.theatre.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class BattleScribe extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    //Receive a dead letter message and process it.
    @Override
    public Receive createReceive() {
        return null;
    }

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void postStop() throws Exception {
    }
}
