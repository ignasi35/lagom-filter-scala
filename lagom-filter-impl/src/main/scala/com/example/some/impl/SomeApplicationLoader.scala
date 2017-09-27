package com.example.some.impl

import com.example.some.api.SomeService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.EssentialFilter

class SomeApplicationLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new SomeApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new SomeApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[SomeService])
}


abstract class SomeApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  override lazy val httpFilters: Seq[EssentialFilter] = Seq(SomeFilter())

  override lazy val lagomServer = serverFor[SomeService](wire[SomeServiceImpl])
}
