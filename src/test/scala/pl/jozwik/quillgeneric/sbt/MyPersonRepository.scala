package pl.jozwik.quillgeneric.sbt

import pl.jozwik.quillgeneric.quillmacro.sync.Repository
import pl.jozwik.quillgeneric.sbt.model.{ Person, PersonId }

trait MyPersonRepository extends Repository[PersonId, Person]
