name := """vtagadmin"""
organization := "vn.m2m"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  javaWs,
  filters,
  "org.mongodb" % "mongo-java-driver" % "3.4.0",
  "org.springframework.data" % "spring-data-mongodb" % "2.0.2.RELEASE",
  "me.prettyprint" % "hector-core" % "1.0-5",
  "com.hazelcast" % "hazelcast-client" % "3.5.2",
  "com.mortennobel" % "java-image-scaling" % "0.8.6",
  "org.json" % "json" % "20160212",
  "org.jsmpp" % "jsmpp" % "2.2.2",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.julienrf" %% "play-jsmessages" % "3.0.0",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "org.apache.commons" % "commons-exec" % "1.3",
  "org.jsoup" % "jsoup" % "1.13.1"
)

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

PlayKeys.devSettings := Seq("play.server.http.port" -> "4444")

routesGenerator := InjectedRoutesGenerator

fork in run := false