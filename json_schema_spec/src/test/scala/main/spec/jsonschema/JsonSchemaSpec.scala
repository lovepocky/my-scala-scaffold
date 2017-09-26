package main.spec.jsonschema

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class JsonSchemaSpec extends FlatSpec with LazyLogging {

  import com.timeout.docless.schema._
  import JsonSchemaSpec._

  "docless" should "generate json schema" in {
    val petSchema = JsonSchema.deriveFor[Pet]
    println(petSchema.asJson.toString())
  }
}

object JsonSchemaSpec {

  case class Pet(id: Int, name: String, tag: Option[String])

}