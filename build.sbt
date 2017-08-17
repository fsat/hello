organization in ThisBuild := "com.lightbend"
version in ThisBuild := "0.1"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `hello` = (project in file("."))
  .aggregate(`hello-api`, `hello-impl`, `hello-stream-api`, `hello-stream-impl`, `external-api`, `external-impl`)

lazy val `hello-api` = (project in file("hello-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslClient
    )
  )


lazy val `hello-impl` = (project in file("hello-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      lagomScaladslClient,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-api`)

lazy val `external-api` = (project in file("external-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `external-impl` = (project in file("external-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslClient,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`external-api`)


lazy val `hello-stream-api` = (project in file("hello-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-stream-impl` = (project in file("hello-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-stream-api`, `hello-api`)

lagomUnmanagedServices in ThisBuild := Map("spring" -> "http://localhost:8080")