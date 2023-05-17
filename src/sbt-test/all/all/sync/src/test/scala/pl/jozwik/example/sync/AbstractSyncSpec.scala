package pl.jozwik.example.sync

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.{ AbstractSpec, PoolHelper }
import pl.jozwik.example.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.repository.DateQuotes

trait AbstractSyncSpec extends AbstractSpec with BeforeAndAfterAll {
  sys.props.put("quill.binds.log", "true")
  lazy protected val ctx               = new H2JdbcContext(SnakeCase, TryHelperSpec.cfg) with DateQuotes
  protected lazy val addressRepository = new AddressRepositoryGen(ctx, "Address")

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
