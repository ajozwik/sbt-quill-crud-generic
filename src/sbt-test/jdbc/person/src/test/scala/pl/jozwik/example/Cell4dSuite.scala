package pl.jozwik.example

import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.sync.SyncRepository

trait Cell4dSuite extends AbstractQuillSpec {
  private val repository: SyncRepository[Cell4dId, Cell4d] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
      "Call crud operations " in {
        val entity = Cell4d(Cell4dId(0, 1, 0, 1), false)
        repository.createOrUpdateAndRead(entity) shouldBe 'success

      }
    }
}
