package pl.jozwik.example.cassandra

import com.datastax.driver.core.{ Cluster, Session }
import com.datastax.driver.extras.codecs.jdk8.LocalDateTimeCodec
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.cassandra.model.{ Address, AddressId }

trait AbstractCassandraSpec extends AbstractSpec with BeforeAndAfterAll {
  sys.props.put("quill.binds.log", true.toString)
  protected def createAddress(id: AddressId) = Address(id, "country", "city")

  protected lazy val session = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
    EmbeddedCassandraServerHelper.getSession
  }

  protected val keySpace = "demo"

  override def beforeAll(): Unit = {
    val dataLoader = new CQLDataLoader(session)
    dataLoader.load(new ClassPathCQLDataSet("scripts/create.cql", keySpace))
  }

  override protected def afterAll(): Unit = {
    import org.cassandraunit.utils.EmbeddedCassandraServerHelper
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
    super.afterAll()
  }

}
