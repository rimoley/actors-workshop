package org.rutiger.theatre;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import static akka.pattern.PatternsCS.ask;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import org.rutiger.theatre.actors.exercise.one.Gimli;
import org.rutiger.theatre.actors.exercise.one.Legolas;
import org.rutiger.theatre.actors.exercise.third.BattleScribe;
import org.rutiger.theatre.actors.exercise.two.Battle;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class App {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("workshop");
        ActorRef gimli = system.actorOf(Gimli.props(), "Gimli");
        ActorRef legolas = system.actorOf(Legolas.props(gimli), "Legolas");
        ActorRef scribe = system.actorOf(BattleScribe.props(), "battle_scribe");
        system.actorOf(Battle.props(100, Arrays.asList(legolas, gimli)));
    }
}
