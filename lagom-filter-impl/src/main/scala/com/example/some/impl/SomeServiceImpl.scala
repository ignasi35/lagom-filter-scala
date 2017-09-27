package com.example.some.impl

import com.example.some.api.SomeService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class SomeServiceImpl() extends SomeService {

  override def hello(name: String) = ServiceCall { _ =>
    println(s"Replying with $name")
    Future.successful(s"Hello $name!")
  }
}
