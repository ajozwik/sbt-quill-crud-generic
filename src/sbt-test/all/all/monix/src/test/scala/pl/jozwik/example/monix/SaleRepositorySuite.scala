package pl.jozwik.example.monix

import pl.jozwik.example.domain.model._
import pl.jozwik.example.monix.repository.{ PersonRepositoryGen, ProductRepositoryGen, SaleRepositoryGen }

trait SaleRepositorySuite extends AbstractJdbcMonixSpec {

  private val repository        = new SaleRepositoryGen(ctx, "Sale")
  private val personRepository  = new PersonRepositoryGen(ctx, "Person3")
  private val productRepository = new ProductRepositoryGen(ctx, "Product")
  "Sale Repository " should {
      "Call all operations on Sale" in {
        repository.all.runSyncUnsafe() shouldBe Seq()
        val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
        val person           = personRepository.createAndRead(personWithoutId).runSyncUnsafe()
        val productWithoutId = Product(ProductId.empty, "productName")
        val product          = productRepository.createAndRead(productWithoutId).runSyncUnsafe()
        val saleId           = SaleId(product.id, person.id)
        val sale             = Sale(saleId, now)
        repository.createAndRead(sale).runSyncUnsafe() shouldBe sale

        repository.createOrUpdateAndRead(sale).runSyncUnsafe() shouldBe sale

        repository.read(saleId).runSyncUnsafe() shouldBe Option(sale)
        repository.delete(saleId).runSyncUnsafe() shouldBe 1
        productRepository.delete(product.id).runSyncUnsafe() shouldBe 1
        personRepository.delete(person.id).runSyncUnsafe() shouldBe 1
      }
    }

}
