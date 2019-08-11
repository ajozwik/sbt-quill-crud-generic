package pl.jozwik.example

import pl.jozwik.example.domain.model.{ Address, AddressId }

trait AddressSuite extends AbstractQuillSpec {
  "Address " should {
    "Batch update address " in {
      val address = Address(AddressId.empty, "Spain", "Warszawa", Option("Podbipiety"))
      addressRepository.create(address) shouldBe 'success
      addressRepository.setCountryIfCity("Warszawa", "Poland") shouldBe 'success
    }
  }
}
