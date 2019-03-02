package controllers

import exceptions.BusinessLogicException
import javax.inject.{Inject, Provider}
import org.slf4j.{Logger, LoggerFactory}
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status.{BAD_REQUEST, NOT_FOUND}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Results.{BadRequest, NotFound}
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api.{Configuration, Environment, OptionalSourceMapper}

import scala.concurrent.Future

class ErrorHandler @Inject()(env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router])
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  lazy val log: Logger = LoggerFactory.getLogger(classOf[ErrorHandler])

  override def onClientError(
                              request: RequestHeader,
                              statusCode: Int,
                              message: String): Future[Result] = statusCode match {

    case BAD_REQUEST =>
      Future.successful {
        val errorMessage = {
          if (message.toLowerCase.contains("invalid json")) {
            "Invalid Json was present in the request"
          } else {
            message
          }
        }
        BadRequest(ErrorHandler.message(errorMessage))
      }

    case NOT_FOUND =>
      Future.successful {
        val message = s"${request.uri} Bad URL"
        NotFound(ErrorHandler.message(message))
      }

    case nonClientError => super.onClientError(request, statusCode, message)
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {

    if(exception.isInstanceOf[BusinessLogicException]){
      Future.successful{
        BadRequest(ErrorHandler.message(exception.getMessage))
      }
    } else{
      log.error("Error occurred while processing request", exception)
      super.onServerError(request, exception)
    }


  }
}

object ErrorHandler {


  def message(message: String): JsObject = Json.obj(
    "errors" -> Seq(
      Json.obj(
        "stacktrace" -> message
      )
    )
  )
}
