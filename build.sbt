name := "MultiIndex"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}


publishArtifact in Test := false

pomExtra :=
  <url>http://github.com/joshlemer/MultiIndex</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:joshlemer/multiindex.git</url>
      <connection>scm:git:git@github.com:joshlemer/multiindex.git</connection>
    </scm>
    <developers>
      <developer>
        <id>joshlemer</id>
        <name>Josh Lemer</name>
        <url>http://github.com/joshlemer</url>
      </developer>
    </developers>