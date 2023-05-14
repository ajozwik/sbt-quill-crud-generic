package pl.jozwik.quillgeneric.sbt

import java.time.LocalDate
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.monad.TryJdbcRepository
import pl.jozwik.quillgeneric.sbt.model.{Person, PersonId}

import scala.util.Try

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy] extends TryJdbcRepository[PersonId, Person, Dialect, Naming] {

  import context.*

  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    Try {
      run(q)
    }
  }

  def max: Try[Option[LocalDate]] = Try {
    val r = dynamicSchema.map(p => p.birthDate)
    context.run(r.max)
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
