package example

import monix.execution.Scheduler.Implicits.global
import scala.concurrent.duration.Duration

import org.http4s.circe.jsonOf
import cats.effect.IO
import cats.kernel.Monoid
import cats.kernel.instances.StringMonoid
import io.circe.generic.JsonCodec
import io.circe.{Decoder, Json, Printer}
import monix.eval.Task
import org.http4s.circe.CirceInstances
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Headers, Method, Request, Uri}
import org.http4s.client.blaze._
import org.http4s.headers.{Accept, `Content-Type`}
import org.http4s.multipart.{Multipart, Part}

// scalastyle:off underscore.import
import io.circe.syntax._
import io.circe.parser._
import cats.instances.string._
// scalastyle:on underscore.import

final case class SgwDoc(
  doc: Json,
  channels: Set[String],
  _id: String,
  _rev: String
)

object SgwDoc {

  implicit val encoder = io.circe.generic.semiauto.deriveEncoder[SgwDoc]

  implicit val decoder = io.circe.generic.semiauto.deriveDecoder[SgwDoc]
}

object SgwClient {

  val circe = CirceInstances.withPrinter(Printer.noSpaces)

  private def decodeAs[A: Decoder](jsonString: String): Task[A] = {
    decode[A](jsonString).fold(Task.raiseError, Task.pure)
  }

  private def decodeAs[A: Decoder](json: Json): Task[A] = {
    json.as[A].fold(Task.raiseError, Task.pure)
  }

  implicit val sgwDocEntityDecoder: EntityDecoder[Task, SgwDoc] = jsonOf[Task, SgwDoc]

  def extractPart(part: Part[Task]): Task[SgwDoc] = {
    implicit val catsKernelStdMonoidForString: Monoid[String] = new StringMonoid
    for {
      jsonString <- part.body.through(fs2.text.utf8Decode).compile.foldMonoid
      doc <- decodeAs[SgwDoc](jsonString)
    } yield doc
  }
}

object Hello extends App with Localhost {

  import SgwClient._
  import SgwClient.circe._

  // scalastyle:off underscore.import
  import org.http4s._
  // scalastyle:on underscore.import


  val httpClient: Task[Client[Task]] = Http1Client[Task]()

  val body = io.circe.parser.parse("{ \"docs\" : [ { \"id\" : \"entzl117m1mdok2d\" }, { \"id\" : \"entz8kn1wod5n5z7\" }, { \"id\" : \"entyzxp01dw5egp4\" }, { \"id\" : \"enty1n4nlxe2mgd1\" }, { \"id\" : \"entx7pqzoygmnkqr\" }, { \"id\" : \"entx73gz3v4ndo1g\" }, { \"id\" : \"entvypo19q92d664\" }, { \"id\" : \"entrx3p19xjxgeq1\" }, { \"id\" : \"entrvnl958x70e59\" }, { \"id\" : \"entr04n0831lrz4v\" }, { \"id\" : \"entqk8ld83915n9e\" }, { \"id\" : \"entq5ogegj7gy4wj\" }, { \"id\" : \"entpwdd7v26p9qd2\" }, { \"id\" : \"entp1wv82ooknmoq\" }, { \"id\" : \"entoyz9pxnvg735j\" }, { \"id\" : \"ento8j7705p7j72q\" }, { \"id\" : \"entnmvj8o67530ww\" }, { \"id\" : \"entmrp0k219z1zwk\" }, { \"id\" : \"entlww11lp0g16zx\" }, { \"id\" : \"entlr2zqzlrw2ddv\" }, { \"id\" : \"entlq1pkznv6lq60\" }, { \"id\" : \"entk3mlxozoqwx8x\" }, { \"id\" : \"entjz7z5p5nmree7\" }, { \"id\" : \"entjqpmdgx7e0dq8\" }, { \"id\" : \"entjkj0gq8pqpkn7\" }, { \"id\" : \"entgwgw75vk76jn5\" }, { \"id\" : \"entgodnrjrppl8em\" }, { \"id\" : \"entggoxjmpx7q07r\" }, { \"id\" : \"entgexx61e5dg1xg\" }, { \"id\" : \"entg74r592rlg0r0\" }, { \"id\" : \"entg66og761v42j2\" }, { \"id\" : \"enteqzrwro9pey1n\" }, { \"id\" : \"ent9okrx1lrprx29\" }, { \"id\" : \"ent9j5y489delm57\" }, { \"id\" : \"ent8p1plgl812ro9\" }, { \"id\" : \"ent80n10n0jwm546\" }, { \"id\" : \"ent7w59kq9mp59j2\" }, { \"id\" : \"ent5yw0ljpg74z1k\" }, { \"id\" : \"ent5ezo1j4yoql0g\" }, { \"id\" : \"ent4og3jolgrgydw\" }, { \"id\" : \"ent41pvjvk9gg5q5\" }, { \"id\" : \"ent409xkr041zx6m\" }, { \"id\" : \"ent3k7m2lrzleno6\" }, { \"id\" : \"ent37jjz7mw1m669\" }, { \"id\" : \"ent30l0k4norwe3z\" }, { \"id\" : \"ent307yq8lxj66r8\" }, { \"id\" : \"ent2lw2k5llzxlzn\" }, { \"id\" : \"ent1n4jrm68j6poz\" }, { \"id\" : \"ent17jy5gyw7p6y4\" }, { \"id\" : \"ent0v45rly0mo0vv\" }, { \"id\" : \"ent0r3z4lmq1n4pn\" } ] }").toOption.get

  val body2 = io.circe.parser.parse("{ \"docs\" : [ { \"id\" : \"ent0r3z4lmq1n4pn\" }, { \"id\" : \"ent0v45rly0mo0vv\" }, { \"id\" : \"ent17jy5gyw7p6y4\" }, { \"id\" : \"ent1n4jrm68j6poz\" }, { \"id\" : \"ent2lw2k5llzxlzn\" }, { \"id\" : \"ent307yq8lxj66r8\" }, { \"id\" : \"ent30l0k4norwe3z\" }, { \"id\" : \"ent37jjz7mw1m669\" }, { \"id\" : \"ent3k7m2lrzleno6\" }, { \"id\" : \"ent409xkr041zx6m\" }, { \"id\" : \"ent41pvjvk9gg5q5\" }, { \"id\" : \"ent4og3jolgrgydw\" }, { \"id\" : \"ent5ezo1j4yoql0g\" }, { \"id\" : \"ent5yw0ljpg74z1k\" }, { \"id\" : \"ent7w59kq9mp59j2\" }, { \"id\" : \"ent80n10n0jwm546\" }, { \"id\" : \"ent8p1plgl812ro9\" }, { \"id\" : \"ent9j5y489delm57\" }, { \"id\" : \"ent9okrx1lrprx29\" }, { \"id\" : \"enteqzrwro9pey1n\" }, { \"id\" : \"entg66og761v42j2\" }, { \"id\" : \"entg74r592rlg0r0\" }, { \"id\" : \"entgexx61e5dg1xg\" }, { \"id\" : \"entggoxjmpx7q07r\" }, { \"id\" : \"entgodnrjrppl8em\" }, { \"id\" : \"entgwgw75vk76jn5\" }, { \"id\" : \"entjkj0gq8pqpkn7\" }, { \"id\" : \"entjqpmdgx7e0dq8\" }, { \"id\" : \"entjz7z5p5nmree7\" }, { \"id\" : \"entk3mlxozoqwx8x\" }, { \"id\" : \"entlq1pkznv6lq60\" }, { \"id\" : \"entlr2zqzlrw2ddv\" }, { \"id\" : \"entlww11lp0g16zx\" }, { \"id\" : \"entmrp0k219z1zwk\" }, { \"id\" : \"entnmvj8o67530ww\" }, { \"id\" : \"ento8j7705p7j72q\" }, { \"id\" : \"entoyz9pxnvg735j\" }, { \"id\" : \"entp1wv82ooknmoq\" }, { \"id\" : \"entpwdd7v26p9qd2\" }, { \"id\" : \"entq5ogegj7gy4wj\" }, { \"id\" : \"entqk8ld83915n9e\" }, { \"id\" : \"entr04n0831lrz4v\" }, { \"id\" : \"entrvnl958x70e59\" }, { \"id\" : \"entrx3p19xjxgeq1\" }, { \"id\" : \"entvypo19q92d664\" }, { \"id\" : \"entx73gz3v4ndo1g\" }, { \"id\" : \"entx7pqzoygmnkqr\" }, { \"id\" : \"enty1n4nlxe2mgd1\" }, { \"id\" : \"entyzxp01dw5egp4\" }, { \"id\" : \"entz8kn1wod5n5z7\" }, { \"id\" : \"entzl117m1mdok2d\" } ] }").toOption.get


  val task = for {
    client <- httpClient

    request <- Request[Task](
      method = Method.POST,
      headers = Headers(`Content-Type`(MediaType.`application/json`)),
      uri = Uri(
        scheme = Some(Uri.Scheme.http),
        authority = Some(
        Uri.Authority(
            host = Uri.IPv4(address = host),
            port = Some(port)
          )
        ),
        path = ""
      )
    ).withBody(body)

    _ = println("here")

    response <- client.fetch(request)(_.as[Multipart[Task]])

    _ = println("here1")

    _ <- Task.gather(
      response.parts.toList.map(extractPart)
    )
  } yield println("Done")

  task.runSyncUnsafe(Duration.Inf)
}

trait Localhost {
  val host = "localhost"
  val port = 8000
}

trait Sgw {
  val host = "sync-gateway.gondor.svc.kube-local.io"
  val port = 4985
}
