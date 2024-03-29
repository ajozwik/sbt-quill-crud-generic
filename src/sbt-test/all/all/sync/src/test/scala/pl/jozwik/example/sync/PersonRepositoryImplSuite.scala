package pl.jozwik.example.sync

import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.example.repository.PersonRepositoryGen

import scala.util.{ Failure, Success, Try }
import org.scalatest.TryValues._

trait PersonRepositoryImplSuite extends AbstractSyncSpec {

  private lazy val repository: PersonRepository[Try] = new PersonRepositoryGen(ctx, "Person2")

  private val person = Person(PersonId.empty, "firstName", "lastName", today)

  "PersonRepositoryImpl" should {
    "Call all operations on Person2 with auto generated id" in {

      repository.all shouldBe Success(Seq())
      val personId = repository.create(person).success.value
      val createdPatron = repository.read(personId).success.value.getOrElse(fail())
      repository.update(createdPatron) shouldBe Symbol("success")
      repository.all shouldBe Success(Seq(createdPatron))

      repository.delete(createdPatron.id) shouldBe Symbol("success")
      repository.read(createdPatron.id).success.value shouldBe empty
      repository.all.success.value shouldBe empty
    }

    "Use create/update and read" in {
      val person1 = repository.createAndRead(person).success.value
      person1.id shouldNot be(person)
      val person2 = repository.createOrUpdateAndRead(person1).success.value
      person2 shouldBe person1
    }
  }
}
