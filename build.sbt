val Http4sVersion = "0.18.9"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"
val ScalacheckVersion = "1.14.0"
val DoobieVersion = "0.5.2"
val CirceVersion = "0.9.3"
val MonixVersion = "3.0.0-RC1"

lazy val root = (project in file("."))
  .settings(
    organization := "uk.co.devproltd",
    name := "monix-play-sample",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.5",
    scalacOptions ++= Seq("-Ypartial-unification", "-feature"),
    scalafmtOnCompile in ThisBuild := true,
    libraryDependencies ++= Http4s ++ Seq(
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.scalacheck" %% "scalacheck"     % ScalacheckVersion % Test,
      "org.specs2"     %% "specs2-core"    % Specs2Version % Test
    ) ++ Circe ++ Doobie ++ Monix
  )

val Http4s = Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion
)

val Circe = Seq(
  "io.circe" %% "circe-core"    % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser"  % CirceVersion,
  "io.circe" %% "circe-java8"   % CirceVersion
)
val Doobie = Seq(
  "org.tpolecat" %% "doobie-core"      % DoobieVersion,
  "org.tpolecat" %% "doobie-h2"        % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari"    % DoobieVersion,
  "org.tpolecat" %% "doobie-postgres"  % DoobieVersion,
  "org.tpolecat" %% "doobie-specs2"    % DoobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion
)

val Monix = Seq(
  "io.monix" %% "monix" % MonixVersion
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
)
