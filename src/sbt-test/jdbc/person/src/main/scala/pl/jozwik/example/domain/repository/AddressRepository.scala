package pl.jozwik.example.domain.repository

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.sync.RepositoryWithGeneratedId

import scala.util.Try

trait AddressRepository extends RepositoryWithGeneratedId[AddressId, Address] {
  def setCountryIfCity(city: String, country: String): Try[Unit]
}
