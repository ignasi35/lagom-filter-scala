package com.example.some.impl

import akka.stream.Materializer
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandler}
import play.api.mvc._
import play.mvc.Http

import scala.concurrent.Future

//trait Authentication {
//  def validate(user: String): Boolean = {
//    user match {
//      case "me" => true
//      case _ => false
//    }
//  }
//
//  def getToken(user: String): String = {
//    val split = headerValue.split("-")
//    split(1)
//  }
//
//
//  def getChallenge: String = "" + new Random().nextInt(100000)
//
//}


class SomeFilter(protected val errorHandler: HttpErrorHandler = DefaultHttpErrorHandler)
                (implicit val mat: Materializer)
  extends Filter {

  override def apply(future: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    println("We are in the filter!")

    request.headers.toSimpleMap.foreach {
      case (key, value) => println(s"Header: $key, $value")
    }
    request.headers.get(Http.HeaderNames.AUTHORIZATION) match {
        case Some(header) =>
          println("Required headers are present, now do authentication...")
          // Do your authentication here, or alternatively, inject a dependency that does it for you...
          future(request)
        case None =>
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

