git clean -x -d -f src/sbt-test
PATH=$HOME/bin:$PATH sbt -Dquill.macro.log=false clean test scripted publishSigned sonatypeRelease
