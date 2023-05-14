package pl.jozwik.example.domain.model

import pl.jozwik.quillgeneric.repository.WithId

object ProductId {
  val empty = ProductId(0)
}

final case class ProductId(value: Long) extends AnyVal

final case class Product(id: ProductId, name: String) extends WithId[ProductId]
