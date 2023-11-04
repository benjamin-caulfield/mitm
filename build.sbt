import sbt.Fork.scala

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"


val scalaTestVersion = "3.2.11"
val guavaVersion = "31.1-jre"
val typeSafeConfigVersion = "1.4.2"
val logbackVersion = "1.2.10"
val sfl4sVersion = "2.0.0-alpha5"
val catsVersion = "2.9.0"
val apacheCommonsVersion = "2.13.0"
val scalaParCollVersion = "1.0.4"
val sparkVersion = "3.5.0"

lazy val commonDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % scalaParCollVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
  "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test,
  "com.typesafe" % "config" % typeSafeConfigVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.google.guava" % "guava" % guavaVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.scala-lang" % "scala-library" % "2.13.12",
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0",
  "org.apache.spark" %% "spark-graphx" % "3.5.0",
)

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.12",
    name := "maninthemiddle",
    libraryDependencies ++= commonDependencies,
  )

scalacOptions += "-Ytasty-reader"



