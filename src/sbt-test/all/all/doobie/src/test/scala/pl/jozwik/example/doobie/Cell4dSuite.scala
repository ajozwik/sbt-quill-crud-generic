package pl.jozwik.example.doobie

import doobie.ConnectionIO
import pl.jozwik.example.domain.model.{Cell4d, Cell4dId}
import pl.jozwik.example.doobie.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.repository.Repository

trait Cell4dSuite extends AbstractDoobieSpec {
  private val repository: Repository[ConnectionIO, Cell4dId, Cell4d, Long] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      val id     = Cell4dId(0, 1, 0, 1)
      val entity = Cell4d(id, false)
      repository.createOrUpdateAndRead(entity).runSyncUnsafe() shouldBe entity
      repository.delete(id).runSyncUnsafe() shouldBe 1

    }
  }
}
