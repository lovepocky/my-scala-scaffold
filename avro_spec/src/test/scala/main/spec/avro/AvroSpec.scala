package main.spec.avro

import java.io.BufferedOutputStream

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec

class AvroSpec extends FlatSpec with LazyLogging {

  import AvroSpec._
  import com.sksamuel.avro4s.AvroSchema
  import com.sksamuel.avro4s.AvroOutputStream
  import com.sksamuel.avro4s.SchemaFor

  import ammonite.ops._

  "avro4s" should "case class to avro schema" in {
    val schema = AvroSchema[SchemaCC]
    schema.toString |> println
  }

  /*it should "add implicit val for recursive schema" in {
    assertThrows[java.lang.StackOverflowError] {
      AvroSchema[SchemaCC]
    }
  }*/

  it should "write instance to json" in {
    implicit val schemaFor = SchemaFor[RecursiveCC]
    implicit val sf = SchemaFor[SchemaCC]

    val data = SchemaCC("name", "id", 1.11, BigDecimal(2.22), None /*, RecursiveCC("hello", None)*/)
    val baos = new java.io.ByteArrayOutputStream()
    val output = AvroOutputStream.json[SchemaCC](baos)
    output.write(data)
    output.close()
    println(baos.toString("UTF-8"))
  }

  it should "json to case class with schema" in {

  }

  it should "write data to file then read it" in {
    import java.io.File
    import com.sksamuel.avro4s.{AvroOutputStream, AvroInputStream}

    val pepperoni = Pizza("pepperoni", Seq(Ingredient("pepperoni", 12, 4.4), Ingredient("onions", 1, 0.4)), false, false, 98)
    val hawaiian = Pizza("hawaiian", Seq(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, false, 91)

    val file = java.io.File.createTempFile("afjewifwjoq", "tmp")

    val os = AvroOutputStream.data[Pizza](file)
    os.write(Seq(pepperoni, hawaiian))
    os.flush()
    os.close()

    val reader = new java.io.FileReader(file)

    val is = AvroInputStream.data[Pizza](file)
    val pizzas = is.iterator.toSet
    is.close()
    println(pizzas.mkString("\n"))
  }

}

object AvroSpec {

  import com.sksamuel.avro4s.SchemaFor
  import com.sksamuel.avro4s.AvroSchemaMerge

  case class SchemaCC(
                       name: String,
                       config_id: String,
                       double: Double = 10,
                       decimal: BigDecimal,
                       opt: Option[String] = None
                       //rec: RecursiveCC
                     )


  case class RecursiveCC(a: String, b: Option[RecursiveCC])


  case class Ingredient(name: String, sugar: Double, fat: Double)

  case class Pizza(name: String, ingredients: Seq[Ingredient], vegetarian: Boolean, vegan: Boolean, calories: Int)

}