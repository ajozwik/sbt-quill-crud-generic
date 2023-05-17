package pl.jozwik.example.zio.impl

import java.time.LocalDateTime
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import zio.Task
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.zio.ZioJdbcRepositoryWithGeneratedId

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends ZioJdbcRepositoryWithGeneratedId[AddressId, Address, Dialect, Naming]
  with AddressRepository[Task] {
  import context.*
  def setCountryIfCity(city: String, country: String): Task[Long] = {

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
