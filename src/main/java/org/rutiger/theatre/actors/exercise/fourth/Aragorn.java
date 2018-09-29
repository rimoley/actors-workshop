package org.rutiger.theatre.actors.exercise.fourth;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import org.rutiger.theatre.Messages;
import org.rutiger.theatre.actors.exercise.Companion;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Aragorn extends Companion {
    private final List<String> names = Arrays.asList("Ernie", "Blaufas", "Pek", "Brotar", "De_la_Cuadra_Salcedo", "Kormak",
    "Chompi", "Jose_Luis", "Tortimer", "Truffles", "Poncho");
    private Integer ghostArmy;
    private Integer armyCount = 0;

    private Aragorn(Integer ghostArmy) {
        this.ghostArmy = ghostArmy;
        attackRouting = createRouter();
    }

    private String ghostName() {
        int position = armyCount % names.size();
        armyCount++;
        return names.get(position) + armyCount;
    }

    private Router attackRouting;

    private Router createRouter() {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < ghostArmy; i++) {
            ActorRef child = getContext().actorOf(Props.create(DunharrowGhost.class), ghostName());
            getContext().watch(child);
            routees.add(new ActorRefRoutee(child));
        }
        return new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public Receive createReceive() {
        return onDuty().orElse(commonBehavior());
    }

    private Receive onDuty() {
        return receiveBuilder()
                .match(Messages.Attack.class, attack -> {
                    attackRouting.route(attack, sender());
                })
                .match(Messages.Killed.class, killed -> {
                    log.info("Ghost soldier {} informed me, {} enemies have been killed", getSender().toString(), killed.orcs());
                    counter += killed.orcs();
                })
                .match(Terminated.class, msg -> {
                    attackRouting = attackRouting.removeRoutee(msg.actor());
                    ActorRef summonedGhost = getContext().actorOf(Props.create(DunharrowGhost.class), ghostName());
                    getContext().watch(summonedGhost);
                    attackRouting = attackRouting.addRoutee(summonedGhost);
                    log.info("I needed to summon a new Ghost!!");
                })
                .match(Messages.EndBattle.class, msg -> {
                    log.info("Hey i killed {} orcs", counter);
                    log.info("Sending away my remaining ghost soldiers...");
                    getContext().getChildren().forEach(child -> {
                            getContext().unwatch(child);
                            attackRouting.removeRoutee(child);
                            log.info("Your task is fullfilled {}", child);
                            getContext().stop(child);
                    });
                    getContext().stop(getSelf());
                })
                .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(3, Duration.ofSeconds(5),
                DeciderBuilder
                        .matchAny(elem -> SupervisorStrategy.restart())
                        .build());
    }

    public static Props props(Integer ghostArmy) {
        return Props.create(Aragorn.class, ghostArmy);
    }
}
