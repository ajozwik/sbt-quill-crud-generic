package pl.jozwik.example.repository

import pl.jozwik.example.model._
import java.time.LocalDate
import scala.util.Try

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming] {

  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) =>
      p.firstName == lift(name) && p.lastName != lift(""))(offset, limit)(dynamicSchema)
  }

  def max: Try[Option[LocalDate]] = Try {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => quote(p.birthDate > lift(date)))(offset, limit)(dynamicSchema)
  }

  def count: Try[Long] = {
    context.count((_: Person) => true)(dynamicSchema)
  }
}
