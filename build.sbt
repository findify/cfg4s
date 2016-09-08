lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  version := "0.0.4",
  organization := "io.findify",
  updateOptions := updateOptions.value.withCachedResolution(true),
  parallelExecution in ThisBuild := false,
  parallelExecution in Test := false,
  libraryDependencies ++= Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.7" % "test"
  ),
  licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
  bintrayOrganization := Some("findify"),
  parallelExecution in Test := false
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
      "com.pszymczyk.consul" % "embedded-consul" % "0.1.10" % "test"
    )
  )

lazy val json = (project in file("json"))
  .dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    name := "cfg4s-json",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-native" % "3.4.0"
    )
  )

lazy val root = (project in file("."))
  .aggregate(core, consul)
    .settings(
      publishArtifact := false,
      bintrayReleaseOnPublish := false,
      bintrayOmitLicense := true
    )
