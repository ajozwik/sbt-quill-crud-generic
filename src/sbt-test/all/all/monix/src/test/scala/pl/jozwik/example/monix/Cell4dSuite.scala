package pl.jozwik.example.monix

import monix.eval.Task
import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.monix.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.Repository

trait Cell4dSuite extends AbstractJdbcMonixSpec {
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
