git clean -x -d -f src/sbt-test
PATH=$HOME/bin:$PATH exec sbt8 -Dquill.macro.log=false clean test scripted publishSigned sonatypeRelease
