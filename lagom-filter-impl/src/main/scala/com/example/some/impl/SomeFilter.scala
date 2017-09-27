package com.example.some.impl

import akka.stream.Materializer
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandler}
import play.api.mvc._
import play.mvc.Http

import scala.concurrent.Future

class SomeFilter(protected val errorHandler: HttpErrorHandler = DefaultHttpErrorHandler)
                  (implicit val mat: Materializer)
  extends Filter {

  override def apply(future: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    println("We are in the filter!")

    request.headers.toSimpleMap.foreach {
      case (key, value) => println(s"Header: key=$key value=$value")
    }
    if (request.headers.toSimpleMap.contains(Http.HeaderNames.AUTHORIZATION)) {
      println("Required headers are present")

      // Do your authentication here, or alternatively, inject a dependency that does it for you...
      future(request)
    }
    else {
      println("Required headers are NOT present")
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

