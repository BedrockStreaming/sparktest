# SparkTest - 0.1.0

**SparkTest** is a Scala library for unit testing with [Spark](https://github.com/apache/spark). 
For now, it is only made for DataFrames. 
It is designed to be used with [ScalaTest](https://github.com/scalatest/scalatest) `Matchers` to do some magic like this:

```scala
import com.bedrock.data.sparktest.{ DataFrameEquality, SparkTestSupport }

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.StringType
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class myTestingClass
  extends AnyFlatSpec
    with Matchers
    with SparkTestSupport
    with DataFrameEquality {
  
  "myMethodToTest" should "make magic with spark" in {
    // This create a new DataFrame with two string typed columns named "company" and "skill".
    val expectedDataFrame: DataFrame = spark.createDF(
      Seq(
        ("bedrock", "streaming"),
        ("bedrock", "drinking"),
        ("mattressstone", "nothing")
      ),
      Seq(
        ("company", StringType),
        ("skill", StringType)
      )
    )
    // This is the result of my method that returns a DataFrame
    val result: DataFrame = myMethodToTest

    // This will compare the two DataFrames
    result shouldEqual expectedDataFrame
  }
}
```

## Compatibility

- Scala 2.12 or later
- ScalaTest all versions
- Spark 3.0.0 or later (under, spark known bugs may appear)

## Quick Start
To use **SparkTest** in an existing maven or sbt project:

### Maven

> WIP
```xml
<repositories>
  ...
  <repository>
    <id>sonatype</id>
    <name>Sonatype Repository</name>
    <url>https://s01.oss.sonatype.org/content/repositories/releases/</url>
  </repository>
  ...
</repositories>

<dependencies>
  ...
  <dependency>
    <groupId>com.bedrockstreaming</groupId>
    <artifactId>sparktest_2.12</artifactId>
    <version>0.1.0</version>
    <scope>test</scope>
  </dependency>
  ...
</dependencies>
```

### SBT

```scala
resolvers += "sonatype".at(
  "https://s01.oss.sonatype.org/content/repositories/releases"
)

libraryDependencies += "com.bedrockstreaming" % "sparktest_2.12" % "0.1.0" % "test"
```

## Tools
### SparkTestSupport
This small `trait` provides a simple SparkSession with log set to warnings and let you focus only on your tests and not on the technical needs to create them.

Example:
```scala
import com.bedrockstreaming.data.sparktest.SparkTestSupport

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainSpec 
  extends AnyFlatSpec
  with Matchers
  with SparkTestSupport {

  "main" should "do stuff" in {
    # A SparkSession `spark` is built in trait `SparkTestSupport`
    import spark.implicits._
    
    // ...
  }
}
```

### DataFrameEquality
This `trait` allows you to compare two DataFrames ignoring the nullability of the fields in the schema (as Spark may be inconsistent with it).
To use it, simply extends `DataFrameEquality` in your testing class.

Example:
```scala
import com.bedrockstreaming.data.sparktest.{ DataFrameEquality, SparkTestSupport }

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainSpec 
  extends AnyFlatSpec
  with Matchers
  with DataFrameEquality
  with SparkTestSupport {

  "main" should "do stuff" in {
    # A SparkSession `spark` is built in trait `SparkTestSupport`
    import spark.implicits._
    
    val df1 = Seq(("id1", 42)).toDF("id", "age")
    val df2 = Seq((42, "id1")).toDF("age", "id")

    df1 shouldEqual df2
  }
}
```

In order to have a better expected output when the test is false, `DataFrameEquality` extends `CustomPrettifier` which provides a custom error message. 
The main advantage is when you're testing whithin IntelliJ IDEA:

![](doc/IntelliJOutput.gif)

### SparkTestTools
This `object` gives you an extension method thanks to an `implicit class` to create DataFrame from a simple `Seq` of data, 
no matter what value you want (as, for now, Spark is not working very well when creating null, for example). 
Therefore, you can do as followed:
```scala
import com.bedrock.data.abtest.common.tool.test.SparkTestTools.SparkSessionOps

val myDF: DataFrame = spark.createDF(
      Seq(
        ("Hey", 12, 56L, false, 1.1),
        (null, null, null, null, null)
      ),
      Seq(
        ("columnName1", StringType),
        ("columnName2", IntegerType),
        ("columnName3", LongType),
        ("columnName4", BooleanType),
        ("columnName5", DoubleType)
      )
    )
```

## Contribution
This library was originally created for Bedrock Streaming projects purpose. As we strongly believe in open source, we share it to you.

If you want to learn more about our opinion on open source, you can read the [OSS article](http://tech.m6web.fr/oss/) on our website.

### Deployment 
Currently the deployment is manually handled by maintainers. 
We use [Sonatype](https://central.sonatype.org/publish/publish-guide/#deployment) to publish and host artifacts along
with the plugin [sbt-sonatype](https://github.com/xerial/sbt-sonatype).

To become an authorized member:
* Create a Sonatype account: https://issues.sonatype.org/secure/Signup!default.jspa
* Ask a maintainer to add your username in authorized member of the application
* Create a file `$HOME/.sbt/1.0/plugins/gpg.sbt` with content `addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")`
`
* Create a file `$HOME/.sbt/sonatype_credentials` with content:
```sonatypeBundleRelease
realm=Sonatype Nexus Repository Manager
host=s01.oss.sonatype.org
user=<username>
password=<password>
```
* Create a file `$HOME/.sbt/1.0/sonatype.sbt`

```scala
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
```
* Finally run the command `sbt publishSigned` to publish a new release or snapshot

### Developing

The features available for now are only those we need, but you're welcome to open an issue or pull-request if you need more.

The project is built using [sbt](https://www.scala-sbt.org/)
To ensure good code quality, we use [scalafmt](https://scalameta.org/scalafmt/).

To check compilation
```bash
sbt compile
```

To format code
```bash
sbt scalafmt
```

### Testing

This library is tested with [scalatest](https://www.google.com/search?client=firefox-b-d&q=scalatest).

To launch tests
```bash
sbt test
```

### Pull-request

There is only 2 things we care about :
  * testing
  * coding standards

### Version

We use [SemVer](http://semver.org/) for versioning (version number MAJOR.MINOR.PATCH).
  * MAJOR: incompatible changes needing data catch-up
  * MINOR: every changes other than bugfixes
  * PATCH: bugfix

### Contributors

* [Thomas Bony](https://github.com/thomasbony)
* [Felix Marsault](https://github.com/fmarsault)
* [Quentin Nambot](https://github.com/rinzool)
