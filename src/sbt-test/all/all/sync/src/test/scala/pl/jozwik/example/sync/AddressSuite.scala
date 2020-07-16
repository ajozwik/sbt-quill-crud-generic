package pl.jozwik.example.sync

import pl.jozwik.example.domain.model.{ Address, AddressId }
import org.scalatest.TryValues._

trait AddressSuite extends AbstractSyncSpec {
  "Address " should {
    "Batch update address " in {
      val address = Address(AddressId.empty, "Spain", "Warszawa", Option("Podbipiety"))
      val id      = addressRepository.create(address).success.value
      addressRepository.setCountryIfCity("Warszawa", "Poland") shouldBe Symbol("success")
      addressRepository.delete(id)
    }
  }
}
