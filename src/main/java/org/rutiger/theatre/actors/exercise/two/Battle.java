package org.rutiger.theatre.actors.exercise.two;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.rutiger.theatre.Messages;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Battle extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Integer enemyArmy;
    private List<ActorRef> warriors;
    private Random enemysDen = new Random();

    public Battle(Integer initialEnemyArmy, List<ActorRef> warriors) {
        this.enemyArmy = initialEnemyArmy;
        this.warriors = warriors;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Attack.class, attack -> {
                    Collections.shuffle(warriors);
                    warriors.stream().forEach(warrior -> warrior.tell(new Messages.Attack(), getSelf()));
                })
                .match(Messages.Shot.class, shot -> {
                    killOrcs(getSender(), shot.arrows());
                })
                .match(Messages.Swung.class, swung -> {
                    killOrcs(getSender(), swung.axe());
                })
                .match(Messages.ReinforcementsArrive.class, spawned -> {
                    log.info("{} orcs join the battle", spawned.orcs());
                    enemyArmy += spawned.orcs();
                })
                .match(Messages.EndBattle.class, any -> {
                    enemiesScheduler.cancel();
                    attackOrdering.cancel();
                    warriors.stream().forEach( warrior -> warrior.tell(new Messages.EndBattle(), getSelf()));
                    getContext().stop(getSelf());
                    getContext().getSystem().terminate();
                })
                .build();
    }

    private void killOrcs(ActorRef warrior, Integer quantity) {
        log.info("{} kills as much as {} orcs", warrior.toString(), quantity);
        if (quantity < enemyArmy) warrior.tell(new Messages.Killed(quantity), getSelf());
        else {
            warrior.tell(new Messages.Killed(enemyArmy), getSelf());
            getSelf().tell(new Messages.EndBattle(), ActorRef.noSender());
        }
        enemyArmy -= quantity;
    }

    private Cancellable enemiesScheduler  = getContext().getSystem()
            .scheduler().schedule(Duration.ofSeconds(5),
                    Duration.ofSeconds(5), getSelf(),
                    new Messages.ReinforcementsArrive(enemysDen.nextInt(10) + 1),
                    getContext().getSystem().dispatcher(), ActorRef.noSender());

    private Cancellable attackOrdering = getContext().getSystem()
            .scheduler().schedule(Duration.ofSeconds(1),
                    Duration.ofSeconds(1), getSelf(), new Messages.Attack(),
                    getContext().getSystem().dispatcher(), ActorRef.noSender());

    @Override
    public void postStop() throws Exception {
        attackOrdering.cancel();
        enemiesScheduler.cancel();
    }

    public static Props props(Integer initialEnemyArmy, List<ActorRef> warriors) {
        return Props.create(Battle.class, initialEnemyArmy, warriors);
    }
}
