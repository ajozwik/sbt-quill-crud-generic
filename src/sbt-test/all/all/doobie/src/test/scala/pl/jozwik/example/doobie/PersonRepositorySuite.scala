package pl.jozwik.example.doobie

import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.doobie.repository.PersonRepositoryGen

trait PersonRepositorySuite extends AbstractDoobieSpec {
  "PersonRepository " should {
    "Call all operations on Person with auto generated id and custom field" in {
      val repository = new PersonRepositoryGen(ctx, "Person3")
      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all.runSyncUnsafe() shouldBe Seq()
      val personId         = repository.create(person)
      val personIdProvided = personId.runSyncUnsafe()
      val createdPatron    = repository.read(personIdProvided).runSyncUnsafe().getOrElse(fail())
      repository.update(createdPatron).runSyncUnsafe() shouldBe 1
      repository.all.runSyncUnsafe() shouldBe Seq(createdPatron)
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified     = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified).runSyncUnsafe() shouldBe 1
      repository.read(createdPatron.id).runSyncUnsafe().map(_.birthDate) shouldBe Option(newBirthDate)

      repository.delete(createdPatron.id).runSyncUnsafe() shouldBe 1
      repository.read(createdPatron.id).runSyncUnsafe() shouldBe empty
      repository.all.runSyncUnsafe() shouldBe Seq()

    }
  }
}
