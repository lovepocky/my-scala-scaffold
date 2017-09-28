package main.spec.avro

import com.sksamuel.avro4s.AvroInputStream
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.io.Codec

class AvroUseExample extends FlatSpec with LazyLogging {

  import com.sksamuel.avro4s.AvroSchema
  import com.sksamuel.avro4s.AvroOutputStream
  import com.sksamuel.avro4s.SchemaFor

  import AvroUseExample._
  import ammonite.ops._

  import better.files._
  import java.io.{File => JFile}

  "avro example" should "generate sender schema" in {
    val schema_v1 = AvroSchema[Message]

    File("./avro_spec/src/test/temp/schema_v1.avsc") |> { file =>
      file.createIfNotExists(createParents = true)
      file < schema_v1.toString
    }

    val schema_v2 = AvroSchema[MessageV2]

    File("./avro_spec/src/test/temp/schema_v2.avsc") |> { file =>
      file.createIfNotExists(createParents = true)
      file < schema_v2.toString
    }

  }

  it should "output old data" in {
    val dataFile = File("./avro_spec/src/test/temp/data_v1.avro")
    dataFile.createIfNotExists(createParents = true)

    val message = Message("name1", "field_old_1")

    val out = AvroOutputStream.data[Message](dataFile.toJava)
    out.write(message)
    out.close()
  }

  it should "sender give new data (with schema migration)" in {
    val dataFileNew = File("./avro_spec/src/test/temp/data_v2.avro")
    val messageNew = MessageV2("name2", 10, "field_old_2")
    val out = AvroOutputStream.data[MessageV2](dataFileNew.toJava)
    out.write(messageNew)
    out.close()
  }

  it should "receiver use sender's schema (same schema v1)" in {
    val dataFile = File("./avro_spec/src/test/temp/data_v1.avro")

    val input = AvroInputStream.data[Message](dataFile.toJava)

    val message = input.iterator().toList
    input.close()

    message |> println
  }


  /**
    * Scenario One:
    * - receiver need old data (v1) but sender give new data (v2)
    *
    * reference:
    * - [[https://github.com/sksamuel/avro4s/commit/986d824f2ae6b8c576ad6b23943f172718e82533 Using different schema for reading and writing]]
    */
  it should "receiver use different schema (v1 / v2) to deserialize data with new schema (v2) to get old data (v1)" in {
    val dataFile = File("./avro_spec/src/test/temp/data_v2.avro")

    /**
      * when receiver not upgrade
      */
    AvroInputStream.data[Message](dataFile.toJava) |> { x =>
      try {
        x.iterator().toList
      } finally x.close
    } shouldBe List(Message("name2", "field_old_default"))

    /**
      * when receiver upgraded
      */
    AvroInputStream.data[MessageV2](dataFile.toJava) |> { x =>
      try {
        x.iterator().toList
      } finally x.close()
    } |> {
      _ shouldBe List(MessageV2("name2", 10, "field_old_2"))
    }

  }

  /**
    * Scenario Two
    * - receiver need new data (v2) but sender give old data (v1)
    */
  it should "receiver use different schema (v1 / v2) to deserialize data with old schema (v1) to get new data (v2)" in {
    val dataFile = File("./avro_spec/src/test/temp/data_v1.avro")

    AvroInputStream.data[MessageV2](dataFile.toJava) |> { x =>
      try {
        x.iterator().toList
      } finally x.close()
    } shouldBe List(MessageV2("name1", field_old = "field_old_1"))

  }

  /**
    * complex schema revolution
    */
  it should "support complex field revolution" in {

  }

  /**
    * schema version management/compatibility
    */
  it should "support version" in {

  }

}

object AvroUseExample {

  case class Message(
                      name: String
                      , field_old: String
                    )

  /**
    * add field: field_new
    */
  case class MessageV2(
                        name: String
                        , field_new: Int = 0
                        , field_old: String
                      )

  /**
    * remove field: field_old
    */
  case class MessageV3(
                        name: String
                        , field_new: Int = 0
                      )

}
