package pl.jozwik.example.doobie.impl

import java.time.LocalDateTime
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import doobie.ConnectionIO
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.doobie.DoobieRepositoryWithTransactionWithGeneratedId

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends DoobieRepositoryWithTransactionWithGeneratedId[AddressId, Address, Dialect, Naming]
  with AddressRepository[ConnectionIO] {
  import context.*
  def setCountryIfCity(city: String, country: String): ConnectionIO[Long] = {

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
