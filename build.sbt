name := "my_scala_scaffold"

//autogenerate from dynver
//version := "0.1"

scalaVersion := "2.11.11"

/**
  * common settings
  */
lazy val commonTestSettings: Seq[Def.Setting[_]] = Seq(
  javaOptions in Test ++= Seq(
    "-Dlog4j.debug=true"
    //, "-Dspark.master=local"
  )
)

lazy val commonDependencies =
  libraryDependencies ++= Seq(
    //test
    "org.scalactic" %% "scalactic" % "3.0.1" % "provided"
    , "org.scalatest" %% "scalatest" % "3.0.1" % "provided,test"
    , "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.7.2" % "test"

    //file
    , "com.github.pathikrit" % "better-files_2.11" % "2.17.1"

    //log tool
    , "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

    //misc tools
    , "com.lihaoyi" %% "ammonite-ops" % "1.0.1"

    //json
    , "org.json4s" %% "json4s-jackson" % "3.2.11"
    , "org.json4s" %% "json4s-ext" % "3.2.11"

    //config (HOCON)
    , "com.typesafe" % "config" % "1.3.1"
  )


lazy val root = (project in file("."))
  .settings(commonTestSettings)
  .settings(commonDependencies)

