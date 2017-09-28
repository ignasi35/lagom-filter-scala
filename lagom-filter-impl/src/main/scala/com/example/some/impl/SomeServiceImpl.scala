package com.example.some.impl

import com.example.some.api.SomeService
import com.example.some.filter.SomePrincipal
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import play.api.mvc.Results
import play.mvc.Http

import scala.concurrent.Future

class SomeServiceImpl() extends SomeService {

  override def hello(name: String) = ServerServiceCall { (requestHeader, request) =>
    println(s"\nService handling request $request.\n------------------------------------------")

    requestHeader.headers.foreach(header => println(s"Request Header: ${header._1}, ${header._2}"))
    val response = requestHeader.principal match {
      case Some(SomePrincipal(user, groups)) =>
        s"$user, who is a member of ${groups.mkString(",")} wants to say hello to $name\n"
      case Some(user) =>
        s"Not sure how we got this ${user.getName}"
    }

    val responseHeader = ResponseHeader.Ok
      .withHeader("Server", "Hello service")

    Future.successful(responseHeader, response)
  }
}
