package pl.jozwik.example.async.impl

import java.time.LocalDate
import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.async.PersonRepository
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.async.AsyncJdbcRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming, C]
  with PersonRepository {
  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): Future[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    run(q)
  }

  def maxBirthDate: Future[Option[LocalDate]] = {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Future[Seq[Person]] = {
    val q = dynamicSchema.filter(p => quote(p.birthDate > lift(date))).drop(offset).take(limit)
    run(q)
  }

  def count: Future[Long] =
    run(dynamicSchema.size)
}
