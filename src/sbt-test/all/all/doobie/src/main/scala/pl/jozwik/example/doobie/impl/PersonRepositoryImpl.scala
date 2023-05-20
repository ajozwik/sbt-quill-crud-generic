package pl.jozwik.example.doobie.impl

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.quillgeneric.doobie.DoobieRepositoryWithTransactionWithGeneratedId
import doobie.ConnectionIO
import java.time.LocalDate

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends DoobieRepositoryWithTransactionWithGeneratedId[PersonId, Person, Dialect, Naming]
  with PersonRepository[ConnectionIO] {

  import context.*

  def searchByFirstName(name: String)(offset: Int, limit: Int): ConnectionIO[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    for {
      r <- run(q)
    } yield {
      r
    }
  }

  def maxBirthDate: ConnectionIO[Option[LocalDate]] = {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): ConnectionIO[Seq[Person]] = {
    val q = dynamicSchema.filter(p => quote(p.birthDate > lift(date))).drop(offset).take(limit)
    for {
      r <- run(q)
    } yield {
      r
    }
  }

  def count: ConnectionIO[Long] = {
    val q = dynamicSchema.size
    for {
      s <- run(q)
    } yield {
      s
    }
  }
}
