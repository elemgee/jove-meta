# Jove meta

Meta-project for Jove

[![Build Status](https://travis-ci.org/jove-sh/jove-meta.svg?branch=master)](https://travis-ci.org/jove-sh/jove-meta)

To checkout and build Jove with all its sub-projects, do

    $ git clone --recursive https://github.com/jove-sh/jove-meta.git
    $ cd jove-meta
    $ sbt jove-meta/pack

Then run

    $ ./jove-meta/target/pack/bin/jove-notebook

Can also be used to open Jove and all its sub-projects at once in an IDE.
