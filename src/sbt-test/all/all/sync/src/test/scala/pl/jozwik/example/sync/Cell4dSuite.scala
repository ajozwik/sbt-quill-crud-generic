package pl.jozwik.example.sync

import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.repository.Cell4dRepositoryGen
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.repository.Repository

import scala.util.{ Success, Try }

class Cell4dSuite extends AbstractSyncSpec {
  private val repository: Repository[Try, Cell4dId, Cell4d, Long] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      val id     = Cell4dId(0, 1, 0, 1)
      val entity = Cell4d(id, false)
      repository.createOrUpdateAndRead(entity) shouldBe Success(entity)
      repository.readUnsafe(id).success.value shouldBe entity
      repository.delete(id) shouldBe Success(1)
      intercept[RuntimeException] {
        repository.readUnsafe(id).success.value
      }
    }
  }
}
