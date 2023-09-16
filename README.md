
# Goal

This Spark SQL UDF converts ip addresses to geo locations on a **JDK 8** environment. The MaxMind's geoip database is stored as a local file.

The UDF can be registered as a `Spark SQL UDF` or can be called in a dataframe's `mapPartition`.


Existing projects such as [MaxMind GeoIP 2 Java](https://github.com/maxmind/GeoIP2-java/) and [snowplow's Scala MaxMind](https://github.com/snowplow/scala-maxmind-iplookups) require Java 11 or greater and are not always feasible (certain instances of AWS Glue still require Java 8)

This project creates the Spark UDF function which runs on **JDK 8**


# Build

* `sbt compile` to compile the jar 
* `sbt assembly` to create the assembly jar to distribute on the Spark cluster
*  `build.sbt` defines :
   * scala version is set to 2.12.15
   * Spark version is set to 3.11
*  In case you want to run the local integration test (local Hadoop and Spark), pay attention to the Hadoop cluster version. Hadoop is sensitive to versions of the different components.

Current [requirements for Spark 3.1.1](https://spark.apache.org/docs/3.1.1/)

* Spark runs on Java 8/11, Scala 2.12,
* For the Scala API, Spark 3.1.1 uses Scala 2.12. You will need to use a compatible Scala version (2.12.x).
* spark-core has a transitive dependency on hadoop-common 3.2


## Test Environment 

Note: To run the integration test, JDK8 environment is needed.


* `sbt test` to run the local test (no Spark involved)
* `sbt IntegrationTest / testOnly` sets up a local Hadoop/Spark cluster (no docker)

The integration test starts a local Hadoop / Spark node. The initialization of the `mini cluster` loads some sample data files `src/it/resources/ips` as parquet files and registers the (free downloaded MaxMind database)[https://dev.maxmind.com/geoip/geolite2-free-geolocation-data#accessing-geolite2-free-geolocation-data] to the Spark environment.

```scala
if (SystemUtils.IS_OS_WINDOWS)
      startHdfs(new File("c:\\tmp")) 
      // on Windows, use a tmp folder without spaces
    else
      startHdfs()

    copyFromLocal("src/it/resources/ips", "/ips")
    val spark: SparkSession = getSpark()
    spark.sparkContext.setLogLevel("ERROR")

    spark.sparkContext.addFile("src/it/resources/GeoLite2-City.mmdb")
```


The integration test will use the 2 different methods:

### MapPartition

```scala
df.mapPartitions( iterator => {
      val _lookup: Option[IpCityLookup] = Some(new IpCityLookup(new File(sparkFilename), 1000))
      // row.getString is IP address
      iterator.map( row => {
        val ipLocationShort: Option[IpLocationShort] = IpCityLookupRegister.ip2location(_lookup, row.getString(0))
        (row.getString(0), ipLocationShort)
      })
    })
```

### SQL UDF 

```scala
  // from the table `ips`,  call ip2location in the SQL query 
  val dfWithLocation = spark.sql("""SELECT genericIp, ip2location(genericIp) as location FROM ips""")
```


# Windows: Integration Test on Hadoop Home

Winutils is required when installing Hadoop on Windows environment.

https://github.com/steveloughran/winutils contains versions from 2.6 to 3.0 
https://github.com/steveloughran/winutils contains versions from 2.6.1 to 3.3.5
https://github.com/s911415/apache-hadoop-3.1.0-winutils (3.1.0)

Once you have installed the correct version and added those to your Windows PATH, the integration test will run,

```shell
REM set environment
set JAVA_HOME=c:\java\jdk-8
set PATH=%JAVA_HOME%\bin;%PATH%
set HADOOP_HOME=d:\hadoop\3.1.0
set PATH=%HADOOP_HOME%;%PATH%

REM compile, test and build assembly
sbt IntegrationTest/test assembly
```

## Python: How to register UDF in pyspark 

```python
# add MaxMind Geo file to Spark
spark.sparkContext.addFile("data/common/geoip2/GeoLite2-City.mmdb")

# register the Scala UDF 
spark._jvm.com.c28n.datective.IpCityLookupRegister.register(spark._jsparkSession, "ip2location", "GeoLite2-City.mmdb")

# read a dataframe in a Spark view
spark.read.parquet("data/.../ips").createOrReplaceTempView("ips")

# use the registered UDF in SQL 
spark.sql("SELECT genericIp, ip2location(genericIp) as location FROM ips").show(10, False)
```

