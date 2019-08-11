package pl.jozwik.example.domain.repository

import java.time.LocalDate

import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.RepositoryWithGeneratedId

import scala.util.Try

trait PersonRepository extends RepositoryWithGeneratedId[PersonId, Person] {

  def count: Try[Long]

  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]]

  def maxBirthDate: Try[Option[LocalDate]]

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]]

}
