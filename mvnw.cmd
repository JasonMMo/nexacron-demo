@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven2's home directory
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@IF "%MAVEN_SKIP_RC%" == "" goto skipRcPre
@SET MAVEN_OPTS=%MAVEN_OPTS% %MAVEN_SKIP_RC%
:skipRcPre

@SETLOCAL

@SET ERROR_CODE=0

@REM ==== START VALIDATION ====
@IF NOT "%JAVA_HOME%" == "" goto OkJHome

@echo.
@echo ERROR: JAVA_HOME not set and no 'java' command could be found in your PATH.
@echo.
@echo Please set the JAVA_HOME variable in your environment to match the
@echo location of your Java installation.

@goto error
:OkJHome

@IF exist "%JAVA_HOME%\bin\java.exe" goto init

@echo.
@echo ERROR: JAVA_HOME is set to an invalid directory.
@echo.
@echo JAVA_HOME = "%JAVA_HOME%"
@echo.
@echo Please set the JAVA_HOME variable in your environment to match the
@echo location of your Java installation.

@goto error
@REM ==== END VALIDATION ====

:init

@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM Fallback to current directory if not found.

SET MAVEN_PROJECTBASEDIR=%CD%
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" (
    IF exist "%MAVEN_PROJECTBASEDIR%\.mvn" (
        SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%
    ) ELSE (
        SET MAVEN_PROJECTBASEDIR=%~dp0
    )
)

SET MAVEN_OPTS=%MAVEN_OPTS% -Djava.awt.headless=true

IF "%MAVEN_BATCH_PAUSE%"=="on" (
    SET MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.test.skip=false
    SET MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.javadoc.skip=false
    SET MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.site.skip=true
)

IF "%MAVEN_TERMINATE_CMD%"=="on" (
    SET MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.ssl.insecure=true
    SET MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.ssl.allowall=true
)

@REM Provide a "standardized" way to have MAVEN_OPTS set for a command-line
@REM invocation.

IF "%MAVEN_CONFIG%" == "" (
    SET MAVEN_CONFIG=%USERPROFILE%\.m2
)

@REM --config-file parameter.

SET MAVEN_CMD_LINE_ARGS=%MAVEN_CONFIG% %*

@REM Execute Maven
@REM To understand how the call below works, refer to the following Maven Wiki page:
@REM https://maven.apache.org/guides/development/guide-building-maven.html#running_even_as_release_manager
@call "%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_MAIN_CLASS% %MAVEN_CMD_LINE_ARGS%

IF %ERROR_CODE% NEQ 0 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & exit /B %ERROR_CODE%