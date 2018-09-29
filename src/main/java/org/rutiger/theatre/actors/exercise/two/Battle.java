package org.rutiger.theatre.actors.exercise.two;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;

import java.util.List;
import java.util.Random;

public class Battle extends AbstractActor {

    private Integer enemyArmy;
    private List<ActorRef> warriors;
    private Random enemysDen = new Random();

    public Battle(Integer initialEnemyArmy, List<ActorRef> warriors) {
        this.enemyArmy = initialEnemyArmy;
        this.warriors = warriors;
    }

    /*TODO
        1) Send attack message to the warrios
        2) If a response from a warrior arrives, keep the proper count, and reply the warrior
        3) If there are no more enemies, end battle
     */
    @Override
    public Receive createReceive() {
        return null;
    }

    //TODO Implement this methods
    private void killOrcs(ActorRef warrior, Integer quantity) {

    }

    //TODO create scheduler for enemies respawning
    private Cancellable enemiesScheduler;

    //TODO create scheduler for ordering attacks
    private Cancellable attackOrdering;

    /*TODO
       Create messages
        - Killed that contains a integer
        - ReinforcementsArrive with a integer
        - EndBattle message
    */
}
