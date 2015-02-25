# Jove meta

Meta-project for Jove

[![Build Status](https://travis-ci.org/jove-sh/jove-meta.svg?branch=master)](https://travis-ci.org/jove-sh/jove-meta)
[![Join the chat at https://gitter.im/jove-sh/jove-notebook](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jove-sh/jove-notebook?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A meta project for [Jove Notebook](https://github.com/jove-sh/jove-notebook),
[Jove Console](https://github.com/jove-sh/jove-console), their
[Scala](https://github.com/jove-sh/jove-scala),
[Spark](https://github.com/jove-sh/jove-spark), and
[Jupyter](https://github.com/jove-sh/jove-jupyter) kernels,
and the notebook [front-end](https://github.com/jove-sh/jove-jupyter-frontend).

This project allows to build and run the whole Jove project at once, including all its kernels
and its notebook server and console.

To checkout and build it, do

    $ git clone --recursive https://github.com/jove-sh/jove-meta.git
    $ cd jove-meta
    $ sbt jove-meta/pack

To have the Spark kernels work, you'll have to force the scala libraries
in the resulting JAR directory (`jove-meta/target/pack/lib`) to be in 2.11.2, with

    $ rm -f jove-meta/target/pack/lib/scala-*2.11.[45]*
    $ for i in compiler library reflect; do
        wget "https://oss.sonatype.org/content/repositories/releases/org/scala-lang/scala-$i/2.11.2/scala-$i-2.11.2.jar" -O "jove-meta/target/pack/lib/scala-$i-2.11.2.jar"
      done

Then run

    $ ./jove-meta/target/pack/bin/jove-notebook

to launch the notebook server, or

    $ ./jove-meta/target/pack/bin/jove-console

to launch the Ammonite-based console.


--

Sub-projects statuses:

Jove [![Build Status](https://travis-ci.org/jove-sh/jove.svg?branch=master)](https://travis-ci.org/jove-sh/jove)

Jove Console [![Build Status](https://travis-ci.org/jove-sh/jove-console.svg?branch=master)](https://travis-ci.org/jove-sh/jove-console)

Jove Jupyter [![Build Status](https://travis-ci.org/jove-sh/jove-jupyter.svg?branch=master)](https://travis-ci.org/jove-sh/jove-jupyter)

Jove Jupyter Frontend [![Build Status](https://travis-ci.org/jove-sh/jove-jupyter-frontend.svg?branch=master)](https://travis-ci.org/jove-sh/jove-jupyter-frontend)

Jove Notebook [![Build Status](https://travis-ci.org/jove-sh/jove-notebook.svg?branch=master)](https://travis-ci.org/jove-sh/jove-notebook)

Jove Scala [![Build Status](https://travis-ci.org/jove-sh/jove-scala.svg?branch=master)](https://travis-ci.org/jove-sh/jove-scala)

Jove Spark [![Build Status](https://travis-ci.org/jove-sh/jove-spark.svg?branch=master)](https://travis-ci.org/jove-sh/jove-spark)
