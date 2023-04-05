package lco.kafka.perf.tests

import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands
import io.lettuce.core.{ClientOptions, RedisClient, RedisURI}
import reactor.core.Disposable
import zio.ZIO

import java.util.UUID


object ReactiveStramsPerfUtils {

  def redisClient(): ZIO[Any, Nothing, RedisClient] = {
    val cOptions = ClientOptions.builder().autoReconnect(true).build()
    val client: RedisClient = RedisClient.create(RedisURI.Builder.redis("localhost", 6379).build())
    client.setOptions(cOptions)
    ZIO.succeed(client)
  }

  def channelName(environment: String, coPackageName: String, coName: String, suffix: String,
           tenantId: String = "global"): String =
    s"$environment.co.$tenantId.$coPackageName.$coName.$suffix"

  def publishMessage(channelName : String, client: RedisClient) : ZIO[Any, Throwable, Unit] = {
    val publisher: RedisPubSubReactiveCommands[String, String] = client.connectPubSub().reactive()
    for {
      _ <- ZIO.logDebug("Starting reactive streams publisher")

      pFiber = (for {
        key <- ZIO.succeed(UUID.randomUUID())
        value = s"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"
        map: Map[UUID, String] = Map(key -> value)
        rm = publisher.publish(channelName, "message1").subscribe()
      } yield rm).forever.fork.as(ZIO.unit)

      allFibers <- ZIO.foreach((1 to 2).toList)(_ => pFiber)
      _ <- ZIO.collectAllParDiscard(allFibers)
    }
    yield ()
  }


  def processMessage(channelName: String, client: RedisClient): Disposable = {
    val subscriber: RedisPubSubReactiveCommands[String, String] = client.connectPubSub().reactive()
    subscriber.subscribe(channelName).subscribe()
    subscriber.observeChannels().doOnNext { message =>
      System.out.println("Received message: " + message.getMessage)
    }.subscribe()
  }

}
