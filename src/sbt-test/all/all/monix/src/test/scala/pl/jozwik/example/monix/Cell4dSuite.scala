package pl.jozwik.example.monix

import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.monix.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.monix.MonixRepository

trait Cell4dSuite extends AbstractJdbcMonixSpec {
  private val repository: MonixRepository[Cell4dId, Cell4d] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
      "Call crud operations " in {
        val id     = Cell4dId(0, 1, 0, 1)
        val entity = Cell4d(id, false)
        repository.createOrUpdateAndRead(entity).runSyncUnsafe() shouldBe entity
        repository.delete(id).runSyncUnsafe() shouldBe 1

      }
    }
}
