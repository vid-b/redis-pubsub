import lco.gradle.UberJar

plugins {
    scala
    id("lco.gradle.uberJar")
}

group = "lco-kafka-perf-tests"
version = rootProject.version

UberJar {
    mainClass.set("lco.kafka.perf.tests.MainApp")
}

dependencies {
    implementation(project(":configurations"))
    implementation(project(":kafka:kafka-utilities"))

    implementation(libs.scala.s2)

    implementation(libs.zio.s2)
    implementation(libs.zio.streams.s2)
    implementation(libs.zio.logging.s2)
    implementation(libs.zio.logging.slf4j.s2)
    implementation(libs.zio.json.s2)
    implementation(libs.zio.zmx.s2)
    implementation(libs.zio.macros.s2)
    implementation(libs.zio.metrics.s2)
    implementation(libs.zio.opentelemetry.s2)
    implementation(libs.zio.http.s2)
    implementation(libs.zio.config.s2)
    implementation(libs.zio.config.typesafe.s2)

    implementation(libs.kafka.client)

    implementation(libs.logback)
    implementation(libs.slf4j)
    implementation(libs.lettuce.core)
    implementation(libs.reactive.streams)

}