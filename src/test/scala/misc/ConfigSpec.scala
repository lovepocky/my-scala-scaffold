package misc

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class ConfigSpec extends FlatSpec with LazyLogging {

  "config" should "override in 'include'" in {
    /**
      * method: ConfigFactory.load() will load standard config
      * @see [[https://github.com/typesafehub/config#standard-behavior]]
      */
    val standard = ConfigFactory.load()
    assert(
      "override2" == standard.getString("spec.include.field")
    )
  }

}
