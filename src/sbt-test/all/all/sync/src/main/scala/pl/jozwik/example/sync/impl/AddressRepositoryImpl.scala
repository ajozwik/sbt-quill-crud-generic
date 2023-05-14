package pl.jozwik.example.sync.impl

import java.time.LocalDateTime
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{Address, AddressId}
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends TryJdbcRepositoryWithGeneratedId[AddressId, Address, Dialect, Naming]
  with AddressRepository[Try] {

  def setCountryIfCity(city: String, country: String): Try[Long] = Try {
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
