package pl.jozwik.example.monix

import io.getquill.H2MonixJdbcContext
import monix.execution.Scheduler
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.monix.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContext

trait AbstractJdbcMonixSpec extends AbstractSpec with BeforeAndAfterAll {
  protected implicit val scheduler: Scheduler = monix.execution.Scheduler.Implicits.global

  lazy protected val ctx               = new H2MonixJdbcContext(strategy, "h2Monix") with MonixWithContext[Long] with DateQuotes
  protected lazy val addressRepository = new AddressRepositoryGen(ctx)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
