package pl.jozwik.example.async

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.repository.AsyncRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

trait AddressRepository extends AsyncRepositoryWithGeneratedId[AddressId, Address, Long] {
  def setCountryIfCity(city: String, country: String): Future[Long]
}
