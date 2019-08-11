package pl.jozwik.example

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext

trait AbstractQuillSpec extends AbstractSpec with BeforeAndAfterAll {
  lazy protected val ctx = new H2JdbcContext(SnakeCase, "h2") with CrudWithContext with DateQuotes

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
