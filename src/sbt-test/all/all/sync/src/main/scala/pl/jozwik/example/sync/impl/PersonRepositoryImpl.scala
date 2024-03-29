package pl.jozwik.example.sync.impl

import java.time.LocalDate
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{Person, PersonId}
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends TryJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming]
  with PersonRepository[Try] {
  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    Try {
      run(q)
    }
  }

  def maxBirthDate: Try[Option[LocalDate]] = Try {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = dynamicSchema.filter(p => quote(p.birthDate > lift(date))).drop(offset).take(limit)
    Try {
      run(q)
    }
  }

  def count: Try[Long] = Try {
    run(dynamicSchema.size)
  }
}
