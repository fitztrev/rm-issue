val rmtest = (project in file("."))
  .settings(
    assembly / mainClass := Some("Stream")
    // more settings here ...
  )
scalaVersion := "2.13.13"

resolvers += ("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "1.1.0-RC12",
  "org.reactivemongo" %% "reactivemongo-akkastream" % "1.1.0-RC6",
  "org.slf4j" % "slf4j-api" % "2.0.3"
  /* "org.reactivemongo" % "reactivemongo-shaded-native" % "1.1.0-RC6-linux-x86-64" */
  /* "org.reactivemongo" % "reactivemongo-shaded" % "1.1.0-RC6" */
  /* "org.reactivemongo" %% "reactivemongo" % "1.1.0-RC6-SNAPSHOT", */
  /* "org.reactivemongo" %% "reactivemongo-bson-api" % "1.1.0-RC6" */
)

exportJars := true
