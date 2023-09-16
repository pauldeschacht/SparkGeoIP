REM set environment
set JAVA_HOME=c:\java\jdk-8
set PATH=%JAVA_HOME%\bin;%PATH%
set HADOOP_HOME=d:\hadoop\3.1.0
set PATH=%HADOOP_HOME%;%PATH%

REM compile, test and build assembly
sbt clean compile test IntegrationTest/test assembly
