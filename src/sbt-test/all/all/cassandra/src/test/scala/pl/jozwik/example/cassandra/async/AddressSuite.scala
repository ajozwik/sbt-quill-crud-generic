package pl.jozwik.example.cassandra.async

import pl.jozwik.example.cassandra.model.AddressId
import pl.jozwik.example.cassandra.async.repository.AddressRepositoryGen

trait AddressSuite extends AbstractAsyncSpec {

  private lazy val repository = new AddressRepositoryGen(ctx)

  "really simple transformation" should {

      "run async" in {
        val id      = AddressId.random
        val address = createAddress(id)
        repository.create(address).futureValue shouldBe address.id
        repository.createAndRead(address).futureValue shouldBe address
        val v = repository.read(id).futureValue
        v shouldBe Option(address)
        repository.all.futureValue shouldBe Seq(address)
        repository.deleteAll.futureValue
        repository.all.futureValue shouldBe Seq.empty
      }

    }

}
