package spec.akka

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import akka.actor._
import ammonite.ops._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class ReactiveEnterpriseSpec extends FlatSpec with LazyLogging {

  import ReactiveEnterpriseSpec._

  lazy val system = ActorSystem("ReactiveEnterprise")

  lazy val processManagersRef: ActorRef = system.actorOf(Props[ProcessManagers], "processManagers")

  "samples in reactive enterprise" should "actor hierarchy" in {

    assert(processManagersRef.path.parent == ActorPath.fromString("akka://ReactiveEnterprise/user"))

    assert(processManagersRef.path.root == ActorPath.fromString("akka://ReactiveEnterprise"))

    assert(processManagersRef.path.elements == List("user", "processManagers"))

  }

  it should "actor selection" in {
    system.actorSelection("/user/processManagers") |> println
    system.actorSelection("/user/processManagers").anchorPath |> println
    system.actorSelection("/user/processManagers").pathString |> println

  }

  it should "report unhandled message" in {
    processManagersRef ! UnhandledMessage()
    //todo
  }

  //Await.result(system.whenTerminated, Duration.Inf)

}

object ReactiveEnterpriseSpec {

  case class BrokerForLoan(name: String)

  case class UnhandledMessage()

  class ProcessManagers extends Actor {
    override def receive: Receive = {
      case BrokerForLoan(name) => name |> println
    }

    //context.system.eventStream

    override def preStart(): Unit = super.preStart()

    override def postStop(): Unit = super.postStop()

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = super.preRestart(reason, message)

    override def postRestart(reason: Throwable): Unit = super.postRestart(reason)

    override def unhandled(message: Any): Unit = super.unhandled(message)
  }

}
