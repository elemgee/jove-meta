import sbt._, Keys._

object Params {
  val is210 = true
  val scala210VersionStr = "2.10.4"
  val scala211VersionStr = "2.11.2"
  val scalaVersionStr = scala211VersionStr
  val crossScalaVersionsStr = Seq(scala210VersionStr, scala211VersionStr)
  def kernels = Seq(
    MetaJoveScalaBuild.root: ClasspathDep[ProjectReference],
    // MetaJoveScalaBuild.embedded: ClasspathDep[ProjectReference],
    MetaJoveSparkBuild.root12: ClasspathDep[ProjectReference],
    // MetaJoveSparkBuild.embedded12: ClasspathDep[ProjectReference],
    MetaJoveJupyterBuild.root: ClasspathDep[ProjectReference]
  )
}

object MetaJoveBuild extends JoveBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove")
)

object MetaJoveScalaBuild extends JoveScalaBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-scala"),
  joveCoreServerProject = Some { p =>
    p.dependsOn(MetaJoveBuild.kernel)
  }
)

object MetaJoveSparkBuild extends JoveSparkBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-spark"),
  joveScalaCoreProject = Some { p =>
    p.dependsOn(MetaJoveScalaBuild.core)
  },
  joveScalaProject = Some { p =>
    p.dependsOn(MetaJoveScalaBuild.root)
  }
)

object MetaJoveJupyterBuild extends JoveJupyterBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-jupyter"),
  joveCoreProject = Some { p =>
    p.dependsOn(MetaJoveBuild.kernel)
  }
)

object MetaJoveNotebookBuild extends JoveNotebookBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-notebook"),
  joveKernelProject = Some { p =>
    p.dependsOn(MetaJoveBuild.kernel)
  }
)

object MetaJoveJupyterFrontendBuild extends JoveJupyterFrontendBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Seq("2.10.4", "2.11.2"),
  base = file("jove-jupyter-frontend"),
  joveNotebookProject = Some { p =>
    p.dependsOn(MetaJoveNotebookBuild.root)
  }
)

object MetaJoveConsoleBuild extends JoveConsoleBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Seq("2.10.4", "2.11.2"),
  base = file("jove-console"),
  joveKernelProject = Some { p =>
    p.dependsOn(MetaJoveBuild.kernel)
  }
)

object MetaJoveMetaBuild extends JoveMetaBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-meta"),
  joveCliKernelsFrontendProject = Some { p =>
    p.dependsOn(
      Seq[ClasspathDep[ProjectReference]](
        MetaJoveConsoleBuild.root,
        MetaJoveNotebookBuild.root,
        MetaJoveJupyterFrontendBuild.root,
        MetaJoveBuild.kernel
      ) ++ Params.kernels : _*
    )
  }
)

object MetaJoveRootBuild extends Build {
  lazy val root = Project(id = "jove-root", base = file("."))
    .settings(
      scalaVersion := Params.scalaVersionStr,
      crossScalaVersions := Params.crossScalaVersionsStr,
      publishArtifact := false
    )
    .aggregate(
      MetaJoveBuild.root,
      MetaJoveNotebookBuild.root,
      MetaJoveJupyterFrontendBuild.root,
      MetaJoveConsoleBuild.root,
      MetaJoveScalaBuild.core,
      MetaJoveScalaBuild.root,
      MetaJoveScalaBuild.cli,
      MetaJoveScalaBuild.embedded,
      MetaJoveScalaBuild.embeddedCli,
      MetaJoveScalaBuild.playPlugin,
      MetaJoveScalaBuild.playPlugin23,
      MetaJoveSparkBuild.core11,
      MetaJoveSparkBuild.core12,
      MetaJoveSparkBuild.core13,
      MetaJoveSparkBuild.root11,
      MetaJoveSparkBuild.root12,
      MetaJoveSparkBuild.root13,
      MetaJoveSparkBuild.cli11,
      MetaJoveSparkBuild.cli12,
      MetaJoveSparkBuild.cli13,
      MetaJoveSparkBuild.embedded11,
      MetaJoveSparkBuild.embedded12,
      MetaJoveSparkBuild.embedded13,
      MetaJoveSparkBuild.embeddedCli11,
      MetaJoveSparkBuild.embeddedCli12,
      MetaJoveSparkBuild.embeddedCli13,
      MetaJoveSparkBuild.cliBootstrap,
      MetaJoveJupyterBuild.root,
      MetaJoveMetaBuild.root
    )
}

object MetaSbtNotebookBuild extends SbtNotebookBuild(file("sbt-notebook"))
