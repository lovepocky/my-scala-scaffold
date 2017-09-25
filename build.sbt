name := "my_scala_scaffold"

//autogenerate from dynver
//version := "0.1"

scalaVersion := "2.11.11"

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


