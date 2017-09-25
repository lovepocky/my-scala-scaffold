package main.spec.avro

import java.io.BufferedOutputStream

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class AvroSpec extends FlatSpec with LazyLogging {

  import AvroSpec._
  import com.sksamuel.avro4s.AvroSchema
  import com.sksamuel.avro4s.AvroOutputStream
  import com.sksamuel.avro4s.SchemaFor

  "avro4s" should "case class to avro schema" in {
    val schema = AvroSchema[SchemaCC]
    println(schema.toString)
  }

  /*it should "add implicit val for recursive schema" in {
    assertThrows[java.lang.StackOverflowError] {
      AvroSchema[SchemaCC]
    }
  }*/

  it should "write instance to json" in {
    implicit val schemaFor = SchemaFor[RecursiveCC]
    implicit val sf = SchemaFor[SchemaCC]

    val data = SchemaCC("name", "id", 1.11, BigDecimal(2.22), None/*, RecursiveCC("hello", None)*/)
    val baos = new java.io.ByteArrayOutputStream()
    val output = AvroOutputStream.json[SchemaCC](baos)
    output.write(data)
    output.close()
    println(baos.toString("UTF-8"))
  }

  it should "json to case class with schema" in {

  }

}

object AvroSpec {

  import com.sksamuel.avro4s.SchemaFor
  import com.sksamuel.avro4s.AvroSchemaMerge

  case class SchemaCC(
                       name: String,
                       config_id: String,
                       double: Double,
                       decimal: BigDecimal,
                       opt: Option[String]
                       //rec: RecursiveCC
                     )


  case class RecursiveCC(a: String, b: Option[RecursiveCC])

}