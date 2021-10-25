import Dependencies.{Akka, Other, Spark}

name := "CourseworkServer"

version := "0.1"

scalaVersion := "2.12.12"


libraryDependencies ++= Seq(
  Akka.Typed,
  Akka.Http,
  Akka.Stream,
  Spark.Core,
  Spark.Sql,
  Other.gson
)

assemblyMergeStrategy in assembly := {
  case  "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}
