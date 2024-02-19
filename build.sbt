name := "SparkIpGeo"
version := "0.2"

scalaVersion := "2.12.15"
val sparkVersion = "3.1.1"
val hadoopVersion = "3.1.0"  // this must be aligned with the %HADOOP_HOME%

// https://github.com/s911415/apache-hadoop-3.1.0-winutils/tree/master/bin

// Spark 2.4.0 runs on Java 8+, Python 2.7+/3.4+ and R 3.1+. For the Scala API, Spark 2.4.0 uses Scala 2.11. You will need to use a compatible Scala version (2.11.x).

// Spark 2.4.8 runs on Java 8, Python 2.7+/3.4+ and R 3.5+. For the Scala API, Spark 2.4.8 uses Scala 2.12. You will need to use a compatible Scala version (2.12.x).
//scalaVersion := "2.12.12"
//val sparkVersion = "2.4.8"
//val hadoopVersion = "2.7.0"  // this must be aligned with the %HADOOP_HOME%

val slf4jVersion = "1.7.10"

scalacOptions += "-target:jvm-1.8"
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers ++= Seq(
  "apache-snapshots" at "https://repository.apache.org/snapshots/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.github.blemale" %% "scaffeine" % "4.0.2", // use JDK compatible version
  "com.maxmind.db" % "maxmind-db" % "2.0.0",
).map(_.exclude("org.slf4j", "*")) ++ Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion % "provided"
)

lazy val global = project
  .in(file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "it",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "it",
      "org.scalatest" %% "scalatest" % "3.1.0" % "test,it",
      "org.apache.hadoop" % "hadoop-common" % hadoopVersion % "it",
      "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion % "it",
      "org.apache.hadoop" % "hadoop-minicluster" % hadoopVersion % "it",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.4" % "it",
    ),
    assemblyJarName := s"${name.value}_${scalaBinaryVersion.value}-${sparkVersion}_${version.value}.jar",
    assemblyPackageScala / assembleArtifact := false,
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    }
  )

Test / fork := true
Test / parallelExecution := false