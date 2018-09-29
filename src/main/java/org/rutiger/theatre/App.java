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

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class App {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("workshop");
        LoggingAdapter log = Logging.getLogger(system, App.class);
        ActorRef legolas = system.actorOf(Props.create(Legolas.class), "Legolas");
        ActorRef gimli = system.actorOf(Props.create(Gimli.class), "Gimli");
        legolas.tell(new Messages.Attack(), ActorRef.noSender());
        gimli.tell(new Messages.Attack(), ActorRef.noSender());
        gimli.tell(new Messages.Attack(), ActorRef.noSender());

        Timeout timeout = Timeout.create(Duration.ofSeconds(2));
        CompletableFuture futureAttack = ask(gimli, new Messages.Attack(), timeout).toCompletableFuture().toCompletableFuture();
        futureAttack.thenAccept(result -> log.info("Got message!! {}", ((Messages.Swung)result).axe()));
        legolas.tell(new Messages.Attack(), ActorRef.noSender());
        legolas.tell(new Messages.Attack(), ActorRef.noSender());

        legolas.tell(PoisonPill.getInstance(), ActorRef.noSender());
        gimli.tell(PoisonPill.getInstance(), ActorRef.noSender());
        system.terminate();
    }
}
