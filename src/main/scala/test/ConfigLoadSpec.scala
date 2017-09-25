package test

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object ConfigLoadSpec extends LazyLogging {
  def main(args: Array[String]): Unit = {
    val standard = ConfigFactory.load()

    val mode = standard.getBoolean("debug.mode")

    logger.info(s"config: debug.mode: $mode")

  }
}
