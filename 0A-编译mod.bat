@echo off
set y=%date:~0,4%
set month=%date:~5,2%
set day=%date:~8,2%

set hour=%time:~1,1%
set min=%time:~3,2%
set sec=%time:~6,2%

set time=%y%.%month%.%day%_%hour%.%min%.%sec%
java -jar Compress.jar build\libs\backups\%time%.zip src gradle build.gradle gradle.properties gradlew gradlew.bat
java -jar Compress.jar build\libs\Dialog-sources.jar src\main\java\* src\main\resources\*

rmdir /q /s build\classes
rmdir /q /s build\dependency-cache
rmdir /q /s build\resources
rmdir /q /s build\retromapping
rmdir /q /s build\sources
rmdir /q /s build\taskLogs
rmdir /q /s build\tmp
goto start

:start
start build\libs
gradlew build
pause
