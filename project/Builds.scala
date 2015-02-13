import sbt._, Keys._

object Params {
  val is210 = true
  val scala210VersionStr = "2.10.4"
  val scala211VersionStr = "2.11.2"
  val scalaVersionStr = scala211VersionStr
  val crossScalaVersionsStr = Seq(scala210VersionStr, scala211VersionStr)
  def kernels = Seq(
    MetaJoveScalaBuild.cli: ClasspathDep[ProjectReference],
    MetaJoveSparkBuild.cli: ClasspathDep[ProjectReference],
    MetaJoveSparkBuild.cliBootstrap: ClasspathDep[ProjectReference],
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
  joveCoreScalaProject = Some { p =>
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

object MainJoveEmbeddedBuild extends JoveEmbeddedBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-embedded"),
  joveCoreServerScalaProject = Some { p =>
    p.dependsOn(MetaJoveBuild.kernel, MetaJoveScalaBuild.root)
  }
)

object MetaJovePlayPluginBuild extends JovePlayPluginBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-play-plugin"),
  joveEmbeddedProject = Some { p =>
    p.dependsOn(MainJoveEmbeddedBuild.root)
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

object MetaJoveMetaBuild extends JoveMetaBuild(
  scalaVersionStr = Params.scalaVersionStr,
  crossScalaVersionsStr = Params.crossScalaVersionsStr,
  base = file("jove-meta"),
  joveCliKernelsFrontendProject = Some { p =>
    p.dependsOn((MetaJoveNotebookBuild.root: ClasspathDep[ProjectReference]) +: (MetaJoveJupyterFrontendBuild.root: ClasspathDep[ProjectReference]) +: (MetaJoveBuild.kernel: ClasspathDep[ProjectReference]) +: Params.kernels: _*)
  }
)

object MetaJoveRootBuild extends Build {
  lazy val root = Project(id = "jove-root", base = file("."))
    .settings(
      scalaVersion := Params.scalaVersionStr,
      crossScalaVersions := Params.crossScalaVersionsStr,
      publish := (),
      publishLocal := ()
    )
    .aggregate(
      MetaJoveBuild.root,
      MetaJoveNotebookBuild.root,
      MetaJoveJupyterFrontendBuild.root,
      MetaJoveScalaBuild.root,
      MetaJoveScalaBuild.cli,
      MetaJoveSparkBuild.root,
      MetaJoveSparkBuild.cli,
      MetaJoveSparkBuild.cliBootstrap,
      MetaJoveJupyterBuild.root,
      MainJoveEmbeddedBuild.root,
      MainJoveEmbeddedBuild.cli,
      MetaJovePlayPluginBuild.root23,
      MetaJovePlayPluginBuild.root,
      MetaJoveMetaBuild.root
    )
}

object MetaSbtNotebookBuild extends SbtNotebookBuild(file("sbt-notebook"))
