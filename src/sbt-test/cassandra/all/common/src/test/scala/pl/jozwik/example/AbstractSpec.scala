package pl.jozwik.example

import java.time.{ LocalDate, LocalDateTime }

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ Matchers, WordSpecLike }
import pl.jozwik.example.domain.model.AddressId

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Matchers with StrictLogging {
  val TIMEOUT_SECONDS              = 6
  val timeLimit                    = Span(TIMEOUT_SECONDS, Seconds)
  protected val now: LocalDateTime = LocalDateTime.now()
  protected val today: LocalDate   = now.toLocalDate
  protected val strategy           = Strategy.namingStrategy

  protected val (offset, limit) = (0, 100)
  protected val generateId      = true
  protected val addressId       = AddressId(1)
}
