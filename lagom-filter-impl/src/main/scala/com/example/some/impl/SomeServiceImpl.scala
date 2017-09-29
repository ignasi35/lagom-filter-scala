package com.example.some.impl

import com.example.some.api.SomeService
import com.example.some.filter.SomePrincipal
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.Future

class SomeServiceImpl() extends SomeService {

  override def hello(name: String) = ServerServiceCall { (requestHeader, request) =>
    val response = requestHeader.principal match {
      case Some(SomePrincipal(user, groups @ _*)) =>
        s"$user, who is a member of ${groups.mkString(",")} wants to say hello to $name\n"
      case Some(user) =>
        s"Not sure how we got this ${user.getName}"
      case None => s"there's no principal !"
    }

    val responseHeader = ResponseHeader.Ok
      .withHeader("Server", "Hello service")

    Future.successful(responseHeader, response)
  }
}
