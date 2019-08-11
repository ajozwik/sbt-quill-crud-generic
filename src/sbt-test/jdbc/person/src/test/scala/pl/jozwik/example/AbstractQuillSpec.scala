package pl.jozwik.example

import java.time.{ LocalDate, LocalDateTime }

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.domain.model.AddressId
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.example.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.{ CompositeKeyCrudWithContext, CrudWithContext }

trait AbstractQuillSpec extends AbstractSpec with BeforeAndAfterAll {
  sys.props.put("quill.binds.log", "true")
  lazy protected val ctx = new H2JdbcContext(SnakeCase, "h2") with CrudWithContext with DateQuotes
  lazy protected val compositeCtx = new H2JdbcContext(SnakeCase, "h2") with CompositeKeyCrudWithContext with DateQuotes
  protected lazy val addressRepository: AddressRepository = new AddressRepositoryGen(ctx, "Address")
  protected val today: LocalDate = LocalDate.now()
  protected val (offset, limit) = (0, 100)
  protected val generateId = true
  protected val addressId = AddressId(1)
  protected val now = LocalDateTime.now()

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
