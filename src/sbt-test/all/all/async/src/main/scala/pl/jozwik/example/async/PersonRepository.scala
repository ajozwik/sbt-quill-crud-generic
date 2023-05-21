package pl.jozwik.example.async

import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.repository.RepositoryWithGeneratedId

import java.time.LocalDate
import scala.concurrent.Future

trait PersonRepository extends RepositoryWithGeneratedId[Future, PersonId, Person, Long] {

  def count: Future[Long]

  def searchByFirstName(name: String)(offset: Int, limit: Int): Future[Seq[Person]]

  def maxBirthDate: Future[Option[LocalDate]]

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Future[Seq[Person]]

}
