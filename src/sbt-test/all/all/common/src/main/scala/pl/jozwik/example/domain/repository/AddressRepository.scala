package pl.jozwik.example.domain.repository

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.RepositoryWithGeneratedId

trait AddressRepository[F[_]] extends RepositoryWithGeneratedId[F, AddressId, Address, Long] {
  def setCountryIfCity(city: String, country: String): F[Long]
}
