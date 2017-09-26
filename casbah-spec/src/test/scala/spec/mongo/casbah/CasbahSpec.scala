package spec.mongo.casbah

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class CasbahSpec extends FlatSpec with LazyLogging {

  import com.mongodb.casbah.Imports._

  "casbah" should "query" in {
    val mongoClient = MongoClient("192.168.3.13")

    mongoClient.dbNames() foreach println

    val db = mongoClient("roadhoc_test")

    db("addons").find().sort("fejwi" $eq 1)
  }
}
