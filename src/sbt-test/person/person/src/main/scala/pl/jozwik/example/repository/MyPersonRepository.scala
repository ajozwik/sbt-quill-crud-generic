package pl.jozwik.example.repository

import pl.jozwik.quillgeneric.quillmacro.sync.Repository
import pl.jozwik.example.model._

trait MyPersonRepository extends Repository[PersonId, Person]
