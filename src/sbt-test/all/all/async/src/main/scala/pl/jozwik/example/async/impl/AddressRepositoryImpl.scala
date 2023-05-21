package pl.jozwik.example.async.impl

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.async.AddressRepository
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.async.AsyncJdbcRepositoryWithGeneratedId
import pl.jozwik.quillgeneric.monad.RepositoryMonadWithGeneratedId

import java.time.LocalDateTime
import scala.concurrent.Future

trait AddressRepositoryImpl[Dialect <: SqlIdiom, Naming <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepositoryWithGeneratedId[AddressId, Address, Dialect, Naming, C]
  with AddressRepository {

  def setCountryIfCity(city: String, country: String): Future[Long] = {
    import context.*
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
