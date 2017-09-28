organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

lazy val lagomFilter = (project in file("."))
  .aggregate(
    LocalProject("lagomFilterApi"),
    LocalProject("lagomFilterImpl")
  )

lazy val filter = (project in file("filter"))
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional
    )
  )

lazy val lagomFilterApi = (project in file("lagom-filter-api"))
  .settings(
    libraryDependencies += lagomScaladslApi
  )
  .dependsOn(filter)

lazy val lagomFilterImpl = (project in file("lagom-filter-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      macwire,
      filters
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(lagomFilterApi)

// Disable Cassandra and Kafka un DevMode since we don't need either of them in this example.
lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
