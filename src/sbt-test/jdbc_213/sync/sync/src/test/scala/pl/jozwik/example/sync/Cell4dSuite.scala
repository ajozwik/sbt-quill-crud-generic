package pl.jozwik.example.sync

import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.sync.SyncRepository

import scala.util.Success

trait Cell4dSuite extends AbstractSyncSpec {
  private val repository: SyncRepository[Cell4dId, Cell4d] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
      "Call crud operations " in {
        val id     = Cell4dId(0, 1, 0, 1)
        val entity = Cell4d(id, false)
        repository.createOrUpdateAndRead(entity) shouldBe Success(entity)
        repository.delete(id) shouldBe Success(1)
      }
    }
}
