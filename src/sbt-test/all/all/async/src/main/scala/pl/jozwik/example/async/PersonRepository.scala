package pl.jozwik.example.async

import pl.jozwik.example.domain.model.{ Person, PersonId }
import java.time.LocalDate

import pl.jozwik.quillgeneric.quillmacro.async.AsyncRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait PersonRepository extends AsyncRepositoryWithGeneratedId[PersonId, Person] {

  def count(implicit ex: ExecutionContext): Future[Long]

  def searchByFirstName(name: String)(offset: Int, limit: Int)(implicit ex: ExecutionContext): Future[Seq[Person]]

  def maxBirthDate(implicit ex: ExecutionContext): Future[Option[LocalDate]]

  def youngerThan(date: LocalDate)(offset: Int, limit: Int)(implicit ex: ExecutionContext): Future[Seq[Person]]

}
