package spec.spark.distributed

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import spec.tag.TestTags._
import akka.actor._
import org.apache.spark.sql.SparkSession
import ammonite.ops._
import spec.spark.distributed.SparkDistributedQuerySpec.Message.CreateQuery

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SparkDistributedQuerySpec extends FlatSpec with LazyLogging {

  /**
    *
    * links:
    * - [[https://github.com/awesome-spark/awesome-spark]]
    *     - [[https://github.com/cloudera/livy]]
    *     - [[https://github.com/Hydrospheredata/mist]] `spark middleware`
    *         - > Super parallel mode: run Spark contexts in separate JVMs
    * - [[https://www.quora.com/How-can-I-run-multiple-spark-jobs-on-same-spark-context/answer/Elias-Abou-Haydar?srid=u4YVJ]]
    *
    * QA:
    * - SparkContext 能否在多个jvm中共享
    * - SparkSession与SparkContext的对应关系
    */

  lazy val session: SparkSession = SparkSession
    .builder
    //.master("spark://spark:7077")
    .master("local[2]")
    .appName("SparkDistributedQuerySpec")
    .config("spark.sql.shuffle.partitions", "5")
    .getOrCreate()

  "spark distributed query" should "share spark context in multi jvm" taggedAs `need-env` in {
    //todo 现在能做到单个jvm即可, 后面可以参考 Hydrospheredata/mist
  }

  it should "session share context" in {
    val session2 = session.newSession()
    session2.sparkContext == session.sparkContext |> println
    session2.sparkContext.toString |> println
    session.sparkContext.toString |> println
  }

  it should "how to use session's shared state??" in {}

  it should "support spark session/query in actors" taggedAs `need-env` in {
    /**
      * todo
      * attempt 1:
      * 将SparkSession作为Actor的参数传入
      */
    import SparkDistributedQuerySpec._
    import Message._
    val system = ActorSystem("SparkActor")
    val sessionActor = system.actorOf(Props[SessionActor], "sessionActor")
    sessionActor ! CreateQuery(3)
    Await.result(system.whenTerminated, Duration.Inf)
  }

  /**
    * query in actors
    * - should support share query path
    *     - auto ?
    */
  it should "share query path automatically in actors ? (attempt 1: not arrange stream graph)" in {
    /**
      * query actor share spark session
      */
  }

  it should "(attempt 2: make stream graph manually)" in {
    /**
      * query actor share specific demultiplexered stream
      */
  }

}

object SparkDistributedQuerySpec {

  /**
    * Spark Distributed Actor Hierarchy
    *
    * /
    * /user + /system
    * /user/context
    * /user/context/session
    * /user/context/session/query
    */

  object Message {

    case class CreateQuery(num: Int)

  }

  class SessionActor extends Actor {

    lazy val session: SparkSession = SparkSession
      .builder
      //.master("spark://spark:7077")
      .master("local[2]")
      .appName("SparkDistributedQuerySpec")
      .config("spark.sql.shuffle.partitions", "5")
      .getOrCreate()

    import scala.collection.mutable

    lazy val querys: mutable.Map[String, ActorRef] = mutable.Map()

    override def receive: Receive = {
      case CreateQuery(num) =>
        "creating query actor" |> println
        (0 until num).foreach { _ =>
          val queryActor = context.actorOf(Props(new QueryActor(session)))
          querys += queryActor.path.toString -> queryActor
          queryActor ! "println_ds"
        }
      case _ => println("session actor received something")
    }
  }

  class QueryActor(val session: SparkSession) extends Actor {

    import session.implicits._

    lazy val testDS = session.createDataset((0 until 3).map(_ => scala.util.Random.alphanumeric.take(4).mkString))

    override def receive: Receive = {
      case "println_ds" =>
        s"printing dataset, session: ${session.toString}" |> println
        testDS.show()
        context.system.terminate()
      case _ => "query actor received something" |> println
    }

  }

  /**
    * More concrete Hierarchy
    *
    * /
    * /user
    * /user/demultiplexer
    * /user/demultiplexer/query
    */

  object Concrete {

    case class CreateQuery()

    case class StopQuery()

    class DemultiplexerActor extends Actor {
      lazy val session: SparkSession = SparkSession
        .builder
        //.master("spark://spark:7077")
        .master("local[2]")
        .appName("SparkDistributedQuerySpec")
        .config("spark.sql.shuffle.partitions", "5")
        .getOrCreate()

      import scala.collection.mutable

      lazy val querys: mutable.Map[String, ActorRef] = mutable.Map()

      override def receive: Receive = {
        case _ =>
      }
    }

    class QueryActor extends Actor {
      override def receive: Receive = {
        case StopQuery() =>
        case _ =>
      }
    }

  }

}

