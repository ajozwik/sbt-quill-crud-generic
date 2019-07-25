package pl.jozwik.example.repository

import pl.jozwik.example.model._
import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.{ QuillCrudWithContext, JdbcRepository }

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcRepository[PersonId, Person, Dialect, Naming] {

  def max: Option[LocalDate] = {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }
}
