# Jove meta

Meta-project for Jove

[![Build Status](https://travis-ci.org/jove-sh/jove-meta.svg?branch=master)](https://travis-ci.org/jove-sh/jove-meta)

To checkout and build Jove with all its sub-projects, do

    $ git clone --recursive https://github.com/jove-sh/jove-meta.git
    $ cd jove-meta
    $ sbt jove-meta/pack

To have the Spark kernels work, you'll have to force the scala libraries
to be in 2.11.2,

    $ rm -f jove-meta/target/pack/lib/scala-*2.11.[45]*
    $ for i in compiler library reflect; do
        wget "https://oss.sonatype.org/content/repositories/releases/org/scala-lang/scala-$i/2.11.2/scala-$i-2.11.2.jar" -O "jove-meta/target/pack/lib/scala-$i-2.11.2.jar"
      done

Then run

    $ ./jove-meta/target/pack/bin/jove-notebook

Can also be used to open Jove and all its sub-projects at once in an IDE.
