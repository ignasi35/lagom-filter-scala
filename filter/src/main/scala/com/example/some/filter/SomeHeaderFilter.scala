package com.example.some.filter

import java.security.Principal

import com.lightbend.lagom.scaladsl.api.transport._
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import play.api.mvc.Results
import play.mvc.Http

import scala.concurrent.Future

//sealed trait SomePrincipal extends Principal {
//  val userName: String
//  val groups: Vector[String]
//
//  override def getName: String = userName
//  override def implies(subject: Subject): Boolean = false
//}
//
//object SomePrincipal {
//  case class SomeServicelessPrincipal(userName: UUID, groups: Vector[String]) extends SomePrincipal
//  case class SomeServicePrincipal(userName: String, groups: Vector[String], servicePrincipal: ServicePrincipal)
//    extends SomePrincipal with ServicePrincipal {
//    override def serviceName: String = servicePrincipal.serviceName
//  }
//
//  def of(userName: String, principal: Option[Principal]) = {
//    principal match {
//      case Some(servicePrincipal: ServicePrincipal) =>
//        SomePrincipal.SomeServicePrincipal(userName, Vector.empty, servicePrincipal)
////      case other =>
////        SomePrincipal.SomeServicelessPrincipal(userName, Vector.empty)
//    }
//  }
//}

case class SomePrincipal(userName: String, groups: String*) extends Principal {
  override def getName = userName
}


object SomeHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = request

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformServerRequest(request: RequestHeader) = {
    println(s"\ntransforming server request\n------------------------------------------")
//    request.principal match {
//      case Some(userPrincipal: SomePrincipal) => request.withHeader("User-Name", userPrincipal.userName)
//      case other => request
//    }
    request.headers.foreach {
      case (key, value) => println(s"Request Header: $key, $value")
    }
    request.getHeader(Http.HeaderNames.AUTHORIZATION) match {
      case Some(header) =>
        println(s"Required header $header is present, now do authentication...")
        // Do your authentication here, or alternatively, inject a dependency that does it for you...

        request.withPrincipal(SomePrincipal(header, "group1", "group2"))
      case None =>
        println("Required headers are NOT present")
    }
    request
  }

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = {
    println(s"\ntransforming server response\n------------------------------------------")
    request.headers.foreach {
      case (key, value) => println(s"Request Header: $key, $value")
    }
    val responseWithHeaders = response.addHeader(Http.HeaderNames.WWW_AUTHENTICATE, "Negotiate")
    responseWithHeaders.headers.foreach {
      case (key, value) => println(s"Response Header: $key, $value")
    }
    responseWithHeaders
  }


  lazy val Composed = HeaderFilter.composite(SomeHeaderFilter, UserAgentHeaderFilter)
}


object ServerSecurity {

  def authenticated[Request, Response](serviceCall: String => ServerServiceCall[Request, Response]) =
    ServerServiceCall.compose { requestHeader =>
      println(s"authenticated headers: ${requestHeader.headers.mkString(",")}")
      serviceCall("something")
//      requestHeader.principal match {
//        case Some(userPrincipal: SomePrincipal) =>
//          serviceCall(userPrincipal.userName)
//        case other =>
//          throw Forbidden("User not authenticated")
//      }
    }

}

object ClientSecurity {

  /**
    * Authenticate a client request.
    */
  def authenticate(userName: String): RequestHeader => RequestHeader = { request =>
    println("authenticate headers: ${requestHeader.headers.mkString(",")}")
    request
//    request.withPrincipal(SomePrincipal.of(userId, request.principal))
  }
}
