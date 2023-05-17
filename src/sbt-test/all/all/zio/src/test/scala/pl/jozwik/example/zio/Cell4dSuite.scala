package pl.jozwik.example.zio

import zio.Task
import pl.jozwik.example.domain.model.{Cell4d, Cell4dId}
import pl.jozwik.example.zio.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.repository.Repository
import zio.interop.catz.*
trait Cell4dSuite extends AbstractZioSpec {
  private val repository: Repository[Task, Cell4dId, Cell4d, Long] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      val id     = Cell4dId(0, 1, 0, 1)
      val entity = Cell4d(id, false)
      repository.createOrUpdateAndRead(entity).runSyncUnsafe() shouldBe entity
      repository.delete(id).runSyncUnsafe() shouldBe 1

    }
  }
}
