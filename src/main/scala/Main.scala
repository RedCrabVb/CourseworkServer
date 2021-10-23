
//import org.apache.spark.sql.SparkSession

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.google.gson.{Gson, JsonArray, JsonParser}
import org.apache.spark.sql.SparkSession

import java.io.FileWriter
import java.time.LocalDate
import scala.io.StdIn
import scala.io.StdIn.readLine

case class JsonLogData(id: Int, header: String, data: LocalDate, completed: Boolean, price: Int, note: String, category: String)

object Main {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val fileOutputName = "output.txt"
    val fileWrite = new FileWriter(fileOutputName)

    val arrayInputLog = new JsonArray()

    val gson = new Gson()

    val route =
      path("api") {
        post {
          parameter("json") { jsonStr =>
            try {
              val json = gson.fromJson(jsonStr, classOf[JsonLogData])
              println(json)
              arrayInputLog.add(JsonParser.parseString(gson.toJson(json)))
            } catch {
              case e: Exception => e.printStackTrace()
            }
            complete(s"Received data ${jsonStr}")
          }
        }
      }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    fileWrite.write(arrayInputLog.toString)
    fileWrite.flush()
    fileWrite.close()

    val spark = SparkSession.builder.appName("Java Spark").config("spark.master", "local").getOrCreate
    val filesRead = spark.read.json(fileOutputName)

    filesRead.printSchema()

    print("Enter the parameter for grouping: ")

    val parameterStr = readLine()

    filesRead.groupBy(parameterStr).count().show()

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
