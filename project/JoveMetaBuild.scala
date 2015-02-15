import language.implicitConversions
import sbt._, Keys._

class JoveMetaBuild(scalaVersionStr: String, crossScalaVersionsStr: Seq[String], base: File, joveCliKernelsFrontendProject: Option[Project => Project] = None) extends Build {
  private val publishSettings = xerial.sbt.Sonatype.sonatypeSettings ++ Seq(
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    licenses := Seq("LGPL-3.0" -> url("http://www.gnu.org/licenses/lgpl.txt")),
    scmInfo := Some(ScmInfo(url("https://github.com/jove-sh/jove-meta"), "git@github.com:jove-sh/jove-meta.git")),
    pomExtra := {
      <url>https://github.com/jove-sh/jove-meta</url>
      <scm>
        <connection>scm:git:github.com/jove-sh/jove-meta.git</connection>
        <developerConnection>scm:git:git@github.com:jove-sh/jove-meta.git</developerConnection>
        <url>github.com/jove-sh/jove-meta.git</url>
      </scm>
      <developers>
        <developer>
          <id>alexarchambault</id>
          <name>Alexandre Archambault</name>
          <url>https://github.com/alexarchambault</url>
        </developer>
      </developers>
    }
  )

  private val commonSettings = Seq(
    organization := "sh.jove",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scalaVersionStr,
    crossScalaVersions := crossScalaVersionsStr,
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    initialCommands in Compile := "import jove._\n",
    resolvers ++= Seq(
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    )
  ) ++ xerial.sbt.Pack.packSettings ++ publishSettings

  private implicit def projectMethods(p: Project) = new {
    def withJoveCliKernelsFrontend: Project =
      joveCliKernelsFrontendProject match {
        case Some(f) =>
          f(p)
        case None =>
          p.settings(
            libraryDependencies <++= Def.setting(Seq(
              "com.github.alexarchambault.jove" %% "jove-kernel-server" % version.value,
              "com.github.alexarchambault.jove" %% "jove-console" % version.value,
              "com.github.alexarchambault.jove" %% "jove-notebook" % version.value,
              "com.github.alexarchambault.jove" %% "jove-jupyter-frontend" % version.value,
              "com.github.alexarchambault.jove" %% "jove-jupyter" % version.value,
              "com.github.alexarchambault.jove" %% "jove-scala" % version.value,
              "com.github.alexarchambault.jove" %% "jove-spark" % version.value
            ))
          )
      }
  }

  lazy val root = Project(id = "jove-meta", base = base)
    .settings(commonSettings: _*)
    .settings(
      name := "jove-meta",
      libraryDependencies ++= Seq(
        "ch.qos.logback" % "logback-classic" % "1.0.13"
      ),
      xerial.sbt.Pack.packMain := {
        var m = Map(
          "jove-notebook" -> "jove.notebook.JoveNotebook"
        )

        if (!scalaVersion.value.startsWith("2.10."))
          m += "jove-console" -> "jove.console.JoveConsole"

        m
      }
    )
    .withJoveCliKernelsFrontend
}
