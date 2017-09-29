package com.example.some.impl

import akka.stream.Materializer
import play.api.http.{ DefaultHttpErrorHandler, HttpErrorHandler }
import play.api.mvc._
import play.mvc.Http

import scala.concurrent.Future

class SomeFilter(protected val errorHandler: HttpErrorHandler = DefaultHttpErrorHandler)
                (implicit val mat: Materializer)
  extends Filter {

  override def apply(future: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    request.headers.get(Http.HeaderNames.AUTHORIZATION) match {
      case Some(_) =>
        future(request)
      case None =>
        Future.successful(Results.Unauthorized("I am the content!").withHeaders(
          (Http.HeaderNames.WWW_AUTHENTICATE, "Negotiate")
        ))
    }
  }
}

object SomeFilter {
  def apply(errorHandler: HttpErrorHandler = DefaultHttpErrorHandler)
           (implicit mat: Materializer) = new SomeFilter(errorHandler)
}

