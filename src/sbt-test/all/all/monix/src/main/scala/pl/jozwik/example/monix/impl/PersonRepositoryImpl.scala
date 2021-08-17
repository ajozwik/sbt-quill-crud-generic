package pl.jozwik.example.monix.impl

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepositoryWithGeneratedId

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends MonixJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming]
  with PersonRepository[Task] {

  def searchByFirstName(name: String)(offset: Int, limit: Int): Task[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => p.firstName == lift(name) && p.lastName != lift(""))(offset, limit)(dynamicSchema)
  }

  def maxBirthDate: Task[Option[LocalDate]] = {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Task[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => quote(p.birthDate > lift(date)))(offset, limit)(dynamicSchema)
  }

  def count: Task[Long] =
    context.count((_: Person) => true)(dynamicSchema)
}
