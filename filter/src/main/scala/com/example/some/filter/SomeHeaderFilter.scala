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


class SomeHeaderFilter extends HeaderFilter {
  override def transformClientRequest(request: RequestHeader) = request

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader) = response

  override def transformServerRequest(request: RequestHeader) = {
    request.getHeader(Http.HeaderNames.AUTHORIZATION) match {
      case Some(header) =>
        request.withPrincipal(SomePrincipal(header, "group1", "group2"))
      case None =>
        request
    }
  }

  // I removed the code from here since this class is not responsible for setting the Header in the response.
  // A HeaderFilter should only transform data from a format to another. I think the Response Header should be set
  // in the SomeFilter (see the java example I sent yesterday)
  override def transformServerResponse(response: ResponseHeader, request: RequestHeader) = response

}


object ServerSecurity {

  def authenticated[Request, Response](serviceCall: String => ServerServiceCall[Request, Response]) =
    ServerServiceCall.compose { requestHeader =>
      println(s"authenticated headers: ${requestHeader.headers.mkString(",")}")
      serviceCall("something")
    }

}

object ClientSecurity {

  /**
    * Authenticate a client request.
    */
  def authenticate(userName: String): RequestHeader => RequestHeader = { request =>
    println("authenticate headers: ${requestHeader.headers.mkString(", ")}")
    request
    //    request.withPrincipal(SomePrincipal.of(userId, request.principal))
  }
}
