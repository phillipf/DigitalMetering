//import AssemblyKeys._

// sbt-assembly
//assemblySettings

name := "DigitalMetering"

version := "0.1"

scalaVersion in ThisBuild := "2.11.11"

resolvers ++= Seq(
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  "central" at "http://repo1.maven.org/maven2/",
  "mvnrepository" at "http://mvnrepository.com/artifact/",
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)
resolvers += Resolver.bintrayRepo("jroper", "maven")

libraryDependencies ++= Seq(
  "com.univocity" % "univocity-parsers" % "2.6.3",
  "joda-time" % "joda-time" % "2.1",
  "org.tpolecat" %% "doobie-core" % "0.5.2",
  "com.outr" %% "lucene4s" % "1.6.0",
  "org.locationtech.geotrellis" %% "geotrellis-vector" % "1.2.1",
  "org.locationtech.geotrellis" %% "geotrellis-proj4" % "1.2.1",
  "com.socrata" %% "soda-publisher-scala" % "2.0.0",
  "com.rojoma" %% "rojoma-json-v3" % "3.8.0",
  "org.json4s" %% "json4s-native" % "3.6.0-M3",
  "org.json4s" %% "json4s-jackson" % "3.6.0-M3",
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "au.id.jazzy" %% "play-geojson" % "1.5.0",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.apache.commons" % "commons-csv" % "1.5",
  "com.koddi" %% "geocoder" % "1.1.0"
)

