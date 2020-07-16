package pl.jozwik.example.async

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait AddressRepository extends AsyncRepositoryWithGeneratedId[AddressId, Address] {
  def setCountryIfCity(city: String, country: String)(implicit ex: ExecutionContext): Future[Long]
}
