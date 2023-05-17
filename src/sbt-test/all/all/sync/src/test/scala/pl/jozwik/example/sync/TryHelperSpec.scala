package pl.jozwik.example.sync

import io.getquill.JdbcContextConfig
import io.getquill.util.LoadConfig
import pl.jozwik.example.PoolHelper

import javax.sql.DataSource
object TryHelperSpec {
  val cfg: JdbcContextConfig = JdbcContextConfig(LoadConfig(PoolHelper.PoolName))
  val pool: DataSource       = cfg.dataSource
}
