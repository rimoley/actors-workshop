package org.rutiger.theatre.actors.exercise.fourth;

import akka.actor.AbstractActor;
import akka.actor.PoisonPill;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;

import java.util.Random;

/*TODO
Create new warrior, Aragorn. It is an actor router with 3 children
Pickup the policy you want for balancing
Use this actor for create 3 children actor of the new "Warrior"
Use futureAsk pattern
Set up a supervision strategy with your preference in what to do in each case
Battle should respond to the Aragorn actor to keep track
Adapt rest of actor accordingly
 */
public class DunharrowGhost extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final Integer MAX_SLASHES = 2;
    private Random r = new Random();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, attackMessage -> {
                    if (now() % 2 == 0) {
                        Integer slashes = r.nextInt(MAX_SLASHES) + 1;
                        log.info("slashing {} my ghostly sword", slashes);
                        getSender().tell(new GhostSword(3), getSelf());
                    }
                    else {
                        mysteryErrorBox();
                    }
                })
                .match(Messages.Killed.class, killed -> {
                    log.info("Seems I, {}, killed some enemies, I will tell my liege!!", getSelf().toString());
                    getContext().parent().tell(killed, getSelf());
                })
                .matchAny(anyMsg -> {
                    log.info("bliba bluba blerg!!!! (What are you saying {})", getSender().toString());
                })
                .build();
    }

    private void mysteryErrorBox() {
        Integer errorResult = (int)(now() % 3);
        switch(errorResult) {
            case 0: throw new IllegalStateException("Ghost overflow");
            case 1: getSelf().tell(PoisonPill.getInstance(), getSelf());
            case 2: getContext().stop(getSelf());
        }
    }

    private Long now() { return System.currentTimeMillis(); }

    public class GhostSword {
        private Integer slashes;
        public GhostSword(Integer slashes) {
            this.slashes = slashes;
        }

        public Integer slashes() {
            return slashes;
        }
    }
}
