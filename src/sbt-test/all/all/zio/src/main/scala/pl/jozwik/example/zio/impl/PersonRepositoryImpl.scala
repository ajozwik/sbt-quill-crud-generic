package pl.jozwik.example.zio.impl

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.quillgeneric.zio.ZioJdbcRepositoryWithGeneratedId
import zio.Task
import java.time.LocalDate

trait PersonRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends ZioJdbcRepositoryWithGeneratedId[PersonId, Person, Dialect, Naming]
  with PersonRepository[Task] {

  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): Task[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    run(q)
  }

  def maxBirthDate: Task[Option[LocalDate]] = {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Task[Seq[Person]] = {
    val q = dynamicSchema.filter(p => quote(p.birthDate > lift(date))).drop(offset).take(limit)
    run(q)
  }

  def count: Task[Long] = {
    val q = dynamicSchema.size
    for {
      s <- run(q)
    } yield {
      s
    }
  }
}
