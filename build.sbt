name := "fp-patterns"

version := "0.1"

scalaVersion := "2.13.1"

lazy val deps = new {

  val zioV    = "1.0.0-RC17"
  val circeV  = "0.12.3"
  val specs2V = "4.8.1"
  val cats    = "org.typelevel" %% "cats-core" % "2.0.0"
  val droste  = "io.higherkindness" %% "droste-core" % "0.8.0"
  val zio = Seq("dev.zio" %% "zio", "dev.zio" %% "zio-streams")
    .map(_ % zioV)

  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-generic"
  ).map(_ % circeV)

  val specs2 = Seq(
    "org.specs2" %% "specs2-core",
    "org.specs2" %% "specs2-matcher"
  ).map(_ % specs2V)

}

scalacOptions ++= Seq(
  "-language:postfixOps"
)

libraryDependencies ++=
  deps.circe ++
    deps.zio ++
    deps.specs2 ++
    Seq(deps.cats, deps.droste)
