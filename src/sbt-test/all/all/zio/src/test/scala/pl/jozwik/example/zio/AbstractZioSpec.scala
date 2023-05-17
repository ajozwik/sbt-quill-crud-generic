package pl.jozwik.example.zio

import io.getquill.H2ZioJdbcContext
import io.getquill.context.jdbc.{ObjectGenericTimeDecoders, ObjectGenericTimeEncoders}
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.sync.TryHelperSpec
import pl.jozwik.example.zio.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.repository.DateQuotes
import zio.{Task, Unsafe, ZEnvironment}
import zio.interop.catz.*
trait AbstractZioSpec extends AbstractSpec with BeforeAndAfterAll {

  lazy protected val ctx               = new H2ZioJdbcContext(strategy) with ObjectGenericTimeDecoders with ObjectGenericTimeEncoders with DateQuotes
  protected lazy val addressRepository = new AddressRepositoryGen(ctx)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  implicit protected class TaskToTImplicit[T](task: Task[T]) {
    def runSyncUnsafe(): T = unsafe(task)
  }

  protected def unsafe[T](task: Task[T]): T =
    Unsafe.unsafe { implicit unsafe =>
      val io = task.provideEnvironment(ZEnvironment(TryHelperSpec.pool))
      zio.Runtime.default.unsafe.run(io).getOrThrow()
    }

}
