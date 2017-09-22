package misc

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class ConfigSpec extends FlatSpec with LazyLogging {

  "standard config" should "override in 'include'" in {
    /**
      * method: ConfigFactory.load() will load standard config
      *
      * @see [[https://github.com/typesafehub/config#standard-behavior]]
      */
    val standard = ConfigFactory.load()
    assert(
      standard.getString("spec.include.field") == "override2"
    )
  }

  it should "skip not exist config file" in {
    val standard = ConfigFactory.load()
    assert(
      standard.getString("spec.include.skip") == "skip"
    )
  }

  it should "cannot include wildcard template (i cannot find this feature in typesafehub/config)" in {
    val standard = ConfigFactory.load()
    assertThrows[com.typesafe.config.ConfigException.Missing](
      standard.getBoolean("debug.mode")
    )
  }


}
