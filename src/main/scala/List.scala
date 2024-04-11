import scala.concurrent._
import scala.concurrent.duration._
import reactivemongo.api._
import reactivemongo.api.bson._
import reactivemongo.akkastream.cursorProducer
import reactivemongo.api.bson.collection.BSONCollection

import scala.util.Try

object List {
  def main(args: Array[String]): Unit = {
    args match {
      case Array(uri) => letsgo(uri)
      case _ => println("Usage: <uri>")
    }
  }
  def letsgo(mongoUri: String, collName: String = "rmbug2") = {
    implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

    val nbDocs = 2000

    val driver = new AsyncDriver
    val database = for {
      uri <- MongoConnection.fromString(mongoUri)
      con <- driver.connect(uri)
      dn <- Future(uri.db.get)
      db <- con.database(dn)
    } yield db

    val collF: Future[BSONCollection] = database.map(_.collection(collName))

    def list(coll: collection.BSONCollection, readPreference: ReadPreference) =
      coll
        .find(BSONDocument.empty)
        .cursor[BSONDocument](readPreference)
        .collect[List](-1, Cursor.FailOnError[List[BSONDocument]]())


    val run = collF.flatMap { coll =>
      coll.delete.one(BSONDocument())
      coll.insert.many((1 to nbDocs).map(i => BSONDocument("_id" -> i))).flatMap { _ =>
        list(coll, ReadPreference.secondaryPreferred) flatMap { resSecondaryPreferred =>
          list(coll, ReadPreference.primary) map { resPrimaryPreferred =>
            println(s"|total document inserted: $nbDocs")
            println(s"|  total result from Secondary Preferred : ${resSecondaryPreferred.size}")
            println(s"|  total result from Primary Preferred   : ${resPrimaryPreferred.size}")
          }
        }
      }
    }

    Try(Await.result(run, 300.seconds))
    driver.close()
  }
}
