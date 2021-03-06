package pl.jozwik.example.cassandra.monix

import pl.jozwik.example.cassandra.model.AddressId
import pl.jozwik.example.cassandra.monix.repository.AddressRepositoryGen

trait AddressSuite extends AbstractMonixMonixSpec {

  private lazy val repository = new AddressRepositoryGen(ctx)

  "AddressSuite simple transformation" should {

      "run async crud operations" in {
        val id      = AddressId.random
        val address = createAddress(id)
        repository.create(address).runSyncUnsafe()
        repository.create(address).runSyncUnsafe()
        val all = repository.all.runSyncUnsafe()
        logger.debug(s"$all")
        repository.read(id).runSyncUnsafe() shouldBe Option(address)
        repository.all.runSyncUnsafe() shouldBe Seq(address)
        repository.deleteAll.runSyncUnsafe()
        repository.all.runSyncUnsafe() shouldBe Seq.empty
      }

    }

}
