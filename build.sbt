import Dependencies.{Akka, Other, Spark}

name := "CourseworkServer"

version := "0.1"

scalaVersion := "2.13.6"


libraryDependencies ++= Seq(
  Akka.Typed,
  Akka.Http,
  Akka.Stream,
  Spark.Core,
  Spark.Sql,
  Other.gson
)