package pl.jozwik.example.async.impl

import java.time.LocalDate

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.async.PersonRepository
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming, C]
  with PersonRepository {

  def searchByFirstName(name: String)(offset: Int, limit: Int)(implicit ec: ExecutionContext): Future[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => p.firstName == lift(name) && p.lastName != lift(""))(offset, limit)(dynamicSchema, ec)
  }

  def maxBirthDate(implicit ec: ExecutionContext): Future[Option[LocalDate]] = {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int)(implicit ec: ExecutionContext): Future[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => quote(p.birthDate > lift(date)))(offset, limit)(dynamicSchema, ec)
  }

  def count(implicit ec: ExecutionContext): Future[Long] =
    context.count((_: Person) => true)(dynamicSchema, ec)
}
