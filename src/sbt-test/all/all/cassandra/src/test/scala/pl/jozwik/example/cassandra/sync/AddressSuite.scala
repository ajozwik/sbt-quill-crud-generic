package pl.jozwik.example.cassandra.sync

import pl.jozwik.example.cassandra.model.AddressId
import pl.jozwik.example.cassandra.sync.repository.AddressRepositoryGen
import org.scalatest.TryValues._

trait AddressSuite extends AbstractSyncSpec {

  private lazy val repository = new AddressRepositoryGen(ctx)

  "really simple transformation" should {

    "run sync" in {
      val id      = AddressId.random
      val address = createAddress(id)
      repository.create(address)
      repository.create(address)
      val v = repository.read(id).success.value
      v shouldBe Option(address)
      repository.all.success.value shouldBe Seq(address)
      repository.deleteAll.success.value
      repository.all.success.value shouldBe Seq.empty
    }

  }

}
