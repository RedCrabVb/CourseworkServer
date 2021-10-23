import sbt._

object Dependencies {

  private object Version {
    val spark = "3.2.0"
    val sparkSql = "3.2.0"
    val AkkaVersion = "2.6.17"
    val AkkaHttpVersion = "10.2.6"
  }

  object Spark {

    val Core = "org.apache.spark" %% "spark-core" % Version.spark
    val Sql = "org.apache.spark" %% "spark-sql" % Version.sparkSql

  }

  object Akka {

    val Typed = "com.typesafe.akka" %% "akka-actor-typed" % Version.AkkaVersion
    val Stream = "com.typesafe.akka" %% "akka-stream" % Version.AkkaVersion
    val Http = "com.typesafe.akka" %% "akka-http" % Version.AkkaHttpVersion

  }

  object Other {
    val gson = "com.google.code.gson" % "gson" % "2.8.2"
  }
}
