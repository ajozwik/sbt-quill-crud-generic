package pl.jozwik.example

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ Matchers, WordSpecLike }

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Matchers with StrictLogging {
  val TIMEOUT_SECONDS = 6
  val timeLimit       = Span(TIMEOUT_SECONDS, Seconds)
}
