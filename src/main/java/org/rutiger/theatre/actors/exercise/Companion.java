package org.rutiger.theatre.actors.exercise;

import java.util.Random;

public interface Companion {

    Random r = new Random();
    default Integer attackWith(Integer max) { return r.nextInt(max) + 1; }
}