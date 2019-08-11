package pl.jozwik.example

import org.scalatest.TryValues._
import pl.jozwik.example.domain.model.{ Address, Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.example.repository.PersonRepositoryGen

import scala.util.Success

trait PersonRepositorySuite extends AbstractQuillSpec {

  private lazy val repository: PersonRepository = new PersonRepositoryGen(ctx, "Person")

  "Person uses fixed id " should {
    "Call all operations on Person" in {

      val address = Address(addressId, "Poland", "Warsaw", Option("Podbipiety"))
      val person = Person(PersonId(1), "firstName", "lastName", today.minusYears(2), Option(addressId))
      val notExisting = Person(PersonId(2), "firstName", "lastName", today, Option(addressId))
      ctx.transaction {
        repository.all shouldBe Success(Seq())
        val addressIdTry = addressRepository.create(address)
        addressIdTry shouldBe 'success
        val id = addressIdTry.success.value
        val newPerson = person.copy(addressId = Option(id))
        repository.createAndRead(newPerson, false) shouldBe Success(newPerson)
      }
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      repository.createOrUpdateAndRead(person) shouldBe Success(person)
      repository.all shouldBe Success(Seq(person))
      repository.youngerThan(person.birthDate.minusDays(1))(offset, limit) shouldBe Success(Seq(person))
      repository.delete(person.id) shouldBe 'success
      repository.all shouldBe Success(Seq())

      repository.createAndRead(person) shouldBe 'failure

    }
  }
}
