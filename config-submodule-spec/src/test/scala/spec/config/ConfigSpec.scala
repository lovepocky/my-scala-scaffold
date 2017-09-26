package spec.config

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class ConfigSpec extends FlatSpec with LazyLogging {

  "submodule config" should "get root config" in {
    val config = ConfigFactory.load()
    assert(
      config.getBoolean("spec.config.root.bool")
    )
  }

  it should "override root config" in {
    val config = ConfigFactory.load()
    assert(
      config.getBoolean("spec.config.submodule.bool")
    )
  }

}
