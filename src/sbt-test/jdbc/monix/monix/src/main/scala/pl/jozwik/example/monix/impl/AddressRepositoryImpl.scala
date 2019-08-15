package pl.jozwik.example.monix.impl

import java.time.LocalDateTime

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.quillmacro.monix.JdbcMonixRepositoryWithGeneratedId

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcMonixRepositoryWithGeneratedId[AddressId, Address, Dialect, Naming]
  with AddressRepository {

  def setCountryIfCity(city: String, country: String): Task[Long] = {
    import context._
    val now = LocalDateTime.now
    val q = dynamicSchema
      .filter(_.city == lift(city))
      .update(
        setValue(_.country, country),
        setValue(_.updated, Option(now))
      )
    run(q)
  }

}
