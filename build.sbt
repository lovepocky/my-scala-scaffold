import sbt.Keys._
import sbt.addCompilerPlugin

name := "my_scala_scaffold"

//autogenerate from dynver
//version := "0.1"

scalaVersion in ThisBuild := "2.11.11"

/**
  * common settings
  */
lazy val commonTestSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions in Test ++= Seq(
    "-Dlog4j2.debug=true"
    //, "-Dspark.master=local"
  )
)

lazy val commonRuntimeSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Ddebug.mode=true"
  )
)

lazy val commonDependencies =
  libraryDependencies ++= Seq(
    //test
    "org.scalactic" %% "scalactic" % "3.0.1"
    , "org.scalatest" %% "scalatest" % "3.0.1"

    //file
    , "com.github.pathikrit" % "better-files_2.11" % "2.17.1"

    //log tool
    , "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
    //, "org.slf4j" % "slf4j-log4j12" % "1.7.25"
    , "org.apache.logging.log4j" % "log4j" % "2.9.1"
    , "org.apache.logging.log4j" % "log4j-api" % "2.9.1"
    , "org.apache.logging.log4j" % "log4j-core" % "2.9.1"
    , "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.9.1"
    , "org.apache.logging.log4j" % "log4j-jcl" % "2.9.1"

    //misc tools
    , "com.lihaoyi" %% "ammonite-ops" % "1.0.1"

    //json
    , "org.json4s" %% "json4s-jackson" % "3.2.11"
    , "org.json4s" %% "json4s-ext" % "3.2.11"

    //config (HOCON)
    , "com.typesafe" % "config" % "1.3.1"
  )


lazy val root = (project in file("."))
  .settings(commonTestSettings: _*)
  .settings(commonRuntimeSettings: _*)
  .settings(commonDependencies)


lazy val `config-submodule-spec` = (project in file("config-submodule-spec"))
  .dependsOn(root)

lazy val avro_spec = project
  .settings(
    libraryDependencies ++= Seq(
      "com.sksamuel.avro4s" %% "avro4s-core" % "1.8.0"
    )
  )
  .dependsOn(root)

lazy val json_schema_spec = project
  .settings(
    libraryDependencies ++= Seq(
      "com.timeout" %% "docless" % "0.4.0"
    )
  )
  .dependsOn(root)

/*
lazy val `datomic-spec` = project
  .settings(
    libraryDependencies ++= Seq(

    )
  )
  .dependsOn(root)*/

lazy val `longevity-spec` = (project in file("longevity-spec"))
  .settings(
    resolvers += Resolver.bintrayRepo("hseeberger", "maven")
    , scalacOptions ++= Seq(
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked")
    , libraryDependencies ++= {
      val akkaHttpVersion = "10.0.3"
      val akkaHttpJson4sVersion = "1.12.0"
      val longevityVersion = "0.25.1"
      val scalaTestVersion = "3.0.1"
      val scalaTimeVersion = "2.16.0"
      val slf4jSimpleVersion = "1.7.25"
      Seq(
        //"org.slf4j" % "slf4j-simple" % slf4jSimpleVersion,
        //"com.github.nscala-time" %% "nscala-time" % scalaTimeVersion,
        //"com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
        //"de.heikoseeberger" %% "akka-http-json4s" % akkaHttpJson4sVersion,
        //"org.longevityframework" %% "longevity-cassandra-deps" % longevityVersion,
        "org.longevityframework" %% "longevity-mongodb-deps" % longevityVersion,
        //"org.longevityframework" %% "longevity-sqlite-deps" % longevityVersion,
        //"org.scalatest" %% "scalatest" % scalaTestVersion % Test

        "org.longevityframework" %% "longevity" % longevityVersion
      )
    }
    , addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
    , fork in run := true
  )
  .dependsOn(root)

lazy val `casbah-spec` = (project in file("casbah-spec"))
  .settings(
    libraryDependencies ++= Seq(
      "org.mongodb" %% "casbah" % "3.1.1"
    )
  )
  .dependsOn(root)