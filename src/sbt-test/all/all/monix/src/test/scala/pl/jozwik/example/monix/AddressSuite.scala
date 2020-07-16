package pl.jozwik.example.monix

import pl.jozwik.example.domain.model.{ Address, AddressId }

trait AddressSuite extends AbstractJdbcMonixSpec {
  "Address " should {
      "Batch update address " in {
        val address = Address(AddressId.empty, "Spain", "Warszawa", Option("Podbipiety"))
        val id      = addressRepository.create(address).runSyncUnsafe()
        id should not be AddressId.empty
        addressRepository.setCountryIfCity("Warszawa", "Poland").runSyncUnsafe() shouldBe 1
        addressRepository.delete(id).runSyncUnsafe() shouldBe 1
      }
    }
}
