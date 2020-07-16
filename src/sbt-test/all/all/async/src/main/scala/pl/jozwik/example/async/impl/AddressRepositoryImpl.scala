package pl.jozwik.example.async.impl

import java.time.LocalDateTime

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.async.AddressRepository
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepositoryWithGeneratedId[AddressId, Address, Dialect, Naming, C]
  with AddressRepository {

  def setCountryIfCity(city: String, country: String)(implicit ex: ExecutionContext): Future[Long] = {
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
