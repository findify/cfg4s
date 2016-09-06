lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  version := "0.0.1",
  organization := "findify",
  updateOptions := updateOptions.value.withCachedResolution(true),
  parallelExecution in ThisBuild := false,
  parallelExecution in Test := false
)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "cfg4s-core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.11.8",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    )
  )

lazy val consul = (project in file("consul"))
  .dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    name := "cfg4s-consul",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-native" % "3.4.0",
      "com.pszymczyk.consul" % "embedded-consul" % "0.1.9" % "test"
    )
  )