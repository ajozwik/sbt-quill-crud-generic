package pl.jozwik.example.zio

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.zio.repository.AddressRepositoryGen
import zio.interop.catz.*

trait AddressSuite extends AbstractZioSpec {

  private lazy val addressRepository = new AddressRepositoryGen(ctx)

  "Address " should {
    "Batch update address " in {
      val city    = "Rakow"
      val address = Address(AddressId.empty, "Lechia", city, Option("Listopadowa"))
      val id      = addressRepository.create(address).runSyncUnsafe()
      id should not be AddressId.empty
      addressRepository.setCountryIfCity(city, "Poland").runSyncUnsafe() shouldBe 1
      addressRepository.delete(id).runSyncUnsafe() shouldBe 1
    }
  }
}
