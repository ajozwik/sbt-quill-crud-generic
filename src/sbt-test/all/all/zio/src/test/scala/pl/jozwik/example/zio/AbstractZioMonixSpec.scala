package pl.jozwik.example.zio

import io.getquill.H2ZioJdbcContext
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.zio.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.repository.DateQuotes

trait AbstractZioMonixSpec extends AbstractSpec with BeforeAndAfterAll {

  lazy protected val ctx               = new H2ZioJdbcContext(strategy, "h2Monix") with DateQuotes
  protected lazy val addressRepository = new AddressRepositoryGen(ctx)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
