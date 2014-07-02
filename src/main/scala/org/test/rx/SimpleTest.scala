package org.test.rx

import akka.actor.ActorSystem
import rx.lang.scala.{Subscription, Observer, Observable}
import spray.client.pipelining._
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SimpleTest {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    import system.dispatcher

    val pipeline = sendReceive

    val response1 = pipeline(Get("https://api.github.com/users"))
    val response2 = pipeline(Get("https://api.github.com/users/octocat/orgs"))

//    val requestStream = Observable.interval(5.seconds).flatMap{_ =>
//      Observable.items(asObservable(response1), asObservable(response2)).flatMap(intern => intern.map(rsp => rsp.status.value))
//    }.take(2)

    val requestStream = Observable.items(asObservable(response1), asObservable(response2)).flatMap(intern => intern.map(rsp => rsp.status.value))

    val subscriber = new Observer[String] {
      override def onNext(e: String){
        println(s"Next: $e")
      }
      override def onError(e: Throwable){
        println(s"Error: $e")
      }
      override def onCompleted(){
        println("Completed")
        system.shutdown()
      }
    }

    val s = requestStream.subscribe(subscriber)

    s.unsubscribe()
  }

  private def asObservable[T](future: Future[T]): Observable[T] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Observable { observer: Observer[T] =>
      future.onComplete{
        case Success(v) =>
          observer.onNext(v)
          observer.onCompleted()
        case Failure(e) =>
          observer.onError(e)
      }

      new Subscription {
        override def unsubscribe() = {
          println("SUBSCRIBE")
        }
      }
    }
  }

}
