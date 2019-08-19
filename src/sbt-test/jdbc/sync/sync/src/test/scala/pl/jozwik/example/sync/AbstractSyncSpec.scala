package pl.jozwik.example.sync

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext

trait AbstractSyncSpec extends AbstractSpec with BeforeAndAfterAll {
  sys.props.put("quill.binds.log", "true")
  lazy protected val ctx               = new H2JdbcContext(SnakeCase, "h2") with CrudWithContext[Long] with DateQuotes
  protected lazy val addressRepository = new AddressRepositoryGen(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
