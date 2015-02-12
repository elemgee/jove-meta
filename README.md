# Jove meta

Meta-project for Jove

To checkout and build Jove with all its sub-projects, do

    $ git clone --recursive https://github.com/jove-sh/jove-meta.git
    $ cd jove-meta
    $ sbt jove-meta/pack

Then run

    $ ./jove-meta/target/pack/bin/notebook
