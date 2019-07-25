package pl.jozwik.quillgeneric.sbt

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository
import pl.jozwik.quillgeneric.sbt.model.{ Person, PersonId }

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcRepository[PersonId, Person, Dialect, Naming] {
  def max: Option[LocalDate] = {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    context.run(r.max)
  }
}