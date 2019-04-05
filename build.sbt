name := """scala-course-web"""
organization := "io.oriel"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.oriel.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.oriel.binders._"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-feature",
  "-Ypartial-unification",
  // Turns all warnings into errors ;-)
  "-Xfatal-warnings",
  // Enables linter options
  "-Xlint:adapted-args", // warn if an argument list is modified to match the receiver
  "-Xlint:nullary-unit", // warn when nullary methods return Unit
  "-Xlint:nullary-override", // warn when non-nullary `def f()' overrides nullary `def f'
  "-Xlint:infer-any", // warn when a type argument is inferred to be `Any`
  "-Xlint:missing-interpolator", // a string literal appears to be missing an interpolator id
  "-Xlint:doc-detached", // a ScalaDoc comment appears to be detached from its element
  "-Xlint:private-shadow", // a private field (or class parameter) shadows a superclass field
  "-Xlint:type-parameter-shadow", // a local type parameter shadows a type already in scope
  "-Xlint:poly-implicit-overload", // parameterized overloaded implicit methods are not visible as view bounds
  "-Xlint:option-implicit", // Option.apply used implicit view
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit
  "-Xlint:package-object-classes", // Class or object defined in package object
)

libraryDependencies ++= Seq(
  "org.asynchttpclient" % "async-http-client" % "2.8.1",
  "io.monix" %% "monix" % "3.0.0-RC2",
  "com.maxmind.geoip2" % "geoip2" % "2.12.0"
)
