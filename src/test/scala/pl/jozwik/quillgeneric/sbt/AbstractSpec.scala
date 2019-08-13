package pl.jozwik.quillgeneric.sbt

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{ Matchers, WordSpecLike }
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.time.{ Seconds, Span }

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Matchers with StrictLogging {
  val TIMEOUT_SECONDS = 6
  val timeLimit       = Span(TIMEOUT_SECONDS, Seconds)
}
