plugins {
    scala
}

group = "lco-kafka"
version = rootProject.version

dependencies {
    implementation(libs.scala.s2)
}