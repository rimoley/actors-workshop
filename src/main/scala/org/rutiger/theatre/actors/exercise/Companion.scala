package org.rutiger.theatre.actors.exercise

import scala.util.Random

trait Companion {
  private val r: Random = Random
  val attackWith: Int => Int = max => r.nextInt(max) + 1
}
