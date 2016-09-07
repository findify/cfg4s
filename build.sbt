lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  version := "0.0.1",
  organization := "findify",
  updateOptions := updateOptions.value.withCachedResolution(true),
  parallelExecution in ThisBuild := false,
  parallelExecution in Test := false,
  libraryDependencies ++= Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.7" % "test"
  )
)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "cfg4s-core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.11.8"
    )
  )

lazy val consul = (project in file("consul"))
  .dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    name := "cfg4s-consul",
    libraryDependencies ++= Seq(
      "com.github.dcshock" % "consul-rest-client" % "0.11",
      "com.pszymczyk.consul" % "embedded-consul" % "0.1.9" % "test"
    )
  )