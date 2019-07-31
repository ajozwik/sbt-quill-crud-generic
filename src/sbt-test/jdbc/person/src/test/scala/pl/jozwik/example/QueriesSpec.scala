package pl.jozwik.example

import java.time.LocalDate

import org.scalatest.TryValues._
import pl.jozwik.example.model.{ Address, AddressId, Configuration, ConfigurationId, Person, PersonId }
import pl.jozwik.example.repository.AddressRepository

import scala.util.{ Failure, Success, Try }

class QueriesSpec extends AbstractQuillSpec {

  private lazy val personRepositoryAutoIncrement = new PersonRepository(ctx, "Person2")
  private lazy val addressRepository = new AddressRepository(ctx, "Address")

  private val generateId = true
  private val addressId = AddressId(1)

  "QueriesSync " should {
    "Call all operations on Person" in {
      val personRepository = new PersonRepository(ctx, "Person")
      val address = Address(addressId, "Poland", "Warsaw", Option("Podbipiety"))
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now, Option(addressId))
      val notExisting = Person(PersonId(2), "firstName", "lastName", LocalDate.now, Option(addressId))
      ctx.transaction {
        personRepository.all shouldBe Success(Seq())
        val addressIdTry = addressRepository.create(address)
        addressIdTry shouldBe 'success
        val id = addressIdTry.success.value
        personRepository.create(person.copy(addressId = Option(id)), false) shouldBe 'success
      }
      personRepository.read(notExisting.id).success.value shouldBe empty
      personRepository.read(person.id).success.value shouldBe Option(person)
      personRepository.update(person) shouldBe 'success
      personRepository.all shouldBe Success(Seq(person))
      personRepository.delete(person.id) shouldBe 'success
      personRepository.all shouldBe Success(Seq())

    }

    "Call all operations on Person2 with auto generated id" in {
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      personRepositoryAutoIncrement.all shouldBe Success(Seq())
      val personId = personRepositoryAutoIncrement.create(person, generateId) match {
        case Failure(th) =>
          logger.error("", th)
          fail(th.getMessage, th)
        case Success(value) =>
          value
      }
      val createdPatron = personRepositoryAutoIncrement.read(personId).success.value.getOrElse(fail())
      personRepositoryAutoIncrement.update(createdPatron) shouldBe 'success
      personRepositoryAutoIncrement.all shouldBe Success(Seq(createdPatron))

      personRepositoryAutoIncrement.delete(createdPatron.id) shouldBe 'success
      personRepositoryAutoIncrement.read(createdPatron.id).success.value shouldBe empty
      personRepositoryAutoIncrement.all shouldBe Success(Seq())
    }

    "Configuration " in {
      val repository = new ConfigurationRepository(ctx)
      logger.debug("configuration")
      val entity = Configuration(ConfigurationId("firstName"), "lastName")
      val entity2 = Configuration(ConfigurationId("nextName"), "nextName")
      repository.all shouldBe Try(Seq())
      val entityId = repository.create(entity)
      val entityIdProvided = entityId.success.value
      val createdEntity = repository.read(entityIdProvided).success.value.getOrElse(fail())
      repository.update(createdEntity) shouldBe 'success
      repository.all shouldBe Success(Seq(createdEntity))
      val newValue = "newValue"
      val modified = createdEntity.copy(value = newValue)
      repository.update(modified) shouldBe 'success
      repository.read(createdEntity.id).success.value.map(_.value) shouldBe Option(newValue)
      repository.delete(createdEntity.id) shouldBe 'success
      repository.read(createdEntity.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())

      repository.createOrUpdate(entity) shouldBe 'success
      repository.createOrUpdate(entity2) shouldBe 'success
      repository.createOrUpdate(entity2) shouldBe 'success
      repository.update(entity2) shouldBe 'success
      repository.all.success.value should contain theSameElementsAs Seq(entity, entity2)
    }
  }
}
