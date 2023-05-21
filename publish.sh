git clean -x -d -f src/sbt-test
. $HOME/.sbt/.sbt-quill-crud-generic-coverage
sbt8 -Dquill.macro.log=false clean coverage test coverageAggregate && sbt8 coveralls || exit 1
PATH=$HOME/bin:$PATH exec sbt8 -Dquill.macro.log=false clean test scripted publishSigned sonatypeRelease
