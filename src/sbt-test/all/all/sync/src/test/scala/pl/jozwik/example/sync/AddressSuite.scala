package pl.jozwik.example.sync

import pl.jozwik.example.domain.model.{ Address, AddressId }
import org.scalatest.TryValues._

trait AddressSuite extends AbstractSyncSpec {
  "Address " should {
    "Batch update address " in {
      val city    = "Rakow"
      val address = Address(AddressId.empty, "Lechia", city, Option("Listopadowa"))
      val id      = addressRepository.create(address).success.value
      addressRepository.setCountryIfCity(city, "Poland") shouldBe Symbol("success")
      addressRepository.delete(id)
    }
  }
}
