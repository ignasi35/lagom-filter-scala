package com.example.some.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceAcl, ServiceCall}
import com.example.some.filter.SomeHeaderFilter

trait SomeService extends Service {

  def hello(id: String): ServiceCall[NotUsed, String]

  override final def descriptor = {
    import Service._
    named("something")
        .withCalls(
          pathCall("/api/hello/:id", hello _)
        )
      .withHeaderFilter(new SomeHeaderFilter())
      .withAutoAcl(true)
  }
}

