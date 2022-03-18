ThisBuild / organizationName := "bedrockstreaming"
ThisBuild / organizationHomepage := Some(url("https://github.com/BedrockStreaming/"))

ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/BedrockStreaming/sparktest"), "scm:git@github.com:BedrockStreaming/sparktest.git")
)

ThisBuild / developers := List(
  Developer(id = "thomasbony", name = "Thomas BONY", email = "???", url = url("https://github.com/thomasbony")),
  Developer(id = "fmarsault", name = "Félix MARSAULT", email = "???", url = url("https://github.com/fmarsault")),
  Developer(
    id = "rinzool",
    name = "Quentin NAMBOT",
    email = "qnambot@gmail.com",
    url = url("https://github.com/rinzool")
  )
)

ThisBuild / description := "A testing tool for Scala and Spark developers ."

ThisBuild / licenses := List(
  "MIT License" -> new URL("https://github.com/BedrockStreaming/sparktest/blob/main/LICENSE")
)
ThisBuild / homepage := Some(url("https://github.com/BedrockStreaming/sparktest/"))

ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots".at(nexus + "content/repositories/snapshots"))
  else Some("releases".at(nexus + "content/repositories/releases/"))
}
ThisBuild / publishMavenStyle := true
