package pl.jozwik.example

import io.getquill.{ CompositeNamingStrategy, NamingStrategy, SnakeCase, UpperCase }

object Strategy {
  val namingStrategy: CompositeNamingStrategy = NamingStrategy(SnakeCase, UpperCase)
}
