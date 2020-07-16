package pl.jozwik.example.sync

import pl.jozwik.example.ConfigurationRepositoryGen
import pl.jozwik.example.domain.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.sync.SyncRepository

import scala.util.{ Success, Try }
import org.scalatest.TryValues._

trait ConfigurationSuite extends AbstractSyncSpec {

  private lazy val repository: SyncRepository[ConfigurationId, Configuration] = new ConfigurationRepositoryGen(ctx)

  "Configuration " should {
      "Call all operation " in {
        logger.debug("configuration")
        val entity = Configuration(ConfigurationId("firstName"), "lastName")
        repository.all shouldBe Try(Seq())
        val entityId         = repository.create(entity)
        val entityIdProvided = entityId.success.value
        val createdEntity    = repository.read(entityIdProvided).success.value.getOrElse(fail())
        repository.update(createdEntity) shouldBe Symbol("success")
        repository.all shouldBe Success(Seq(createdEntity))
        val newValue = "newValue"
        val modified = createdEntity.copy(value = newValue)
        repository.update(modified) shouldBe Symbol("success")
        repository.read(createdEntity.id).success.value.map(_.value) shouldBe Option(newValue)
        repository.delete(createdEntity.id) shouldBe Symbol("success")
        repository.read(createdEntity.id).success.value shouldBe empty
        repository.all shouldBe Try(Seq())
      }
    }
}
