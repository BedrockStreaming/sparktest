publishMavenStyle := true

licenses := Seq("MIT License" -> url("https://github.com/BedrockStreaming/sparktest/blob/main/LICENSE"))

import xerial.sbt.Sonatype._

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
homepage := Some(url("https://github.com/BedrockStreaming/sparktest"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/BedrockStreaming/sparktest"),
    "scm:git@github.com:BedrockStreaming/sparktest.git"
  )
)

developers := List(
  Developer(id="thomasbony", name="Thomas BONY", email="???", url=url("https://github.com/thomasbony")),
  Developer(id="fmarsault", name="Félix MARSAULT", email="???", url=url("https://github.com/fmarsault")),
  Developer(id="rinzool", name="Quentin NAMBOT", email="qnambot@gmail.com", url=url("https://github.com/rinzool")),
)
