PATH=$HOME/bin:$PATH sbt -Dquill.macro.log=false clean test scripted publishSigned sonatypeRelease
