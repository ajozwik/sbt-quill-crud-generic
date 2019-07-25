package pl.jozwik.example.repository

import pl.jozwik.example.model._
import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.{ QuillCrudWithContext, Repository }

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends Repository[PersonId, Person] {
  protected val context: JdbcContext[Dialect, Naming] with QuillCrudWithContext

  def max: Option[LocalDate] = {
    import context._
    val r = quote {
      query[Person].map(p => p.birthDate)
    }
    context.run(r.max)
  }
}
