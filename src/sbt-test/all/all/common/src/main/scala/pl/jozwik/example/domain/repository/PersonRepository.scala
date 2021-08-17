package pl.jozwik.example.domain.repository

import java.time.LocalDate

import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.RepositoryWithGeneratedId

trait PersonRepository[F[_]] extends RepositoryWithGeneratedId[F, PersonId, Person, Long] {

  def count: F[Long]

  def searchByFirstName(name: String)(offset: Int, limit: Int): F[Seq[Person]]

  def maxBirthDate: F[Option[LocalDate]]

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): F[Seq[Person]]

}
