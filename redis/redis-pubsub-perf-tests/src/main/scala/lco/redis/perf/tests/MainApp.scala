package lco.kafka.perf.tests

import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands
import io.lettuce.core.{ClientOptions, RedisClient}
import io.opentelemetry.api.GlobalOpenTelemetry
import lco.config.kafka.KafkaServiceConfig.KafkaService
import lco.config.kafka.KafkaServiceConfig
import lco.kafka.utils.KafkaTopics
import org.apache.kafka.clients.producer.KafkaProducer
import zio.http.Client
import zio.logging.backend.SLF4J
import zio.telemetry.opentelemetry.Tracing
import zio.{Duration, Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

import scala.concurrent.duration.{DurationInt, SECONDS}

object MainApp extends ZIOAppDefault {
  override def run: ZIO[ZIOAppArgs & Scope, Any, Any] = {

    val tracingLayer: ZLayer[Any, Throwable, Tracing] = ZLayer.succeed(GlobalOpenTelemetry.getTracerProvider.tracerBuilder("test").build()) >>> Tracing.live
    val producerLayer = ZLayer.fromZIO(KafkaPerfProducer.createProducer())

    val layers = ZLayer.makeSome[Scope, Tracing](tracingLayer,  ZLayer.Debug.mermaid)

//    // create Redis client
//    val cOptions = ClientOptions.builder().autoReconnect(true).build()
//    val client: RedisClient = RedisClient.create("redis://localhost")
//    client.setOptions(cOptions)
//
//    // create publisher and subscriber instances
//    val subscriber: RedisPubSubReactiveCommands[String, String] = client.connectPubSub().reactive()
//    val publisher: RedisPubSubReactiveCommands[String, String] = client.connectPubSub().reactive()
//
//    // subscribe to the "channel" and print received messages
//    subscriber.subscribe("channel").subscribe()
//    subscriber.observeChannels().doOnNext { message =>
//      System.out.println("Received message: " + message)
//    }.subscribe()
//
//    // publish two messages to the "channel"
//    publisher.publish("channel", "message1").subscribe()
//    publisher.publish("channel", "message2").subscribe()
//
//    Thread.sleep(100)
//
//    client.shutdown


    (for {
      _ <- ZIO.logDebug("Processing reactive redis streams")
      channelName = ReactiveStramsPerfUtils.channelName("dev", "ain", "perf", "global", "perf")
      client <- ReactiveStramsPerfUtils.redisClient()
      s = ReactiveStramsPerfUtils.processMessage(channelName, client)
      _ <- ReactiveStramsPerfUtils.publishMessage(channelName, client)
      - <- ZIO.sleep(Duration(2, SECONDS))
    } yield ()).provideSome[Scope](layers ++ (Runtime.removeDefaultLoggers >>> SLF4J.slf4j)) *> ZIO.never
  }
}
