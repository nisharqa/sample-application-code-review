@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven's installed home (optional)
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@REM Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@REM Add default JVM options here. You can also use MAVEN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@REM Begin loading all jar files in the .mvn wrapper directory

@setlocal
setlocal enabledelayedexpansion

set WRAPPER_DIR=%APP_HOME%\.mvn
set WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar

@REM Create .mvn directory if it doesn't exist
if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"

@REM Download the maven-wrapper.jar from Maven central repository
if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper from https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar', '%WRAPPER_JAR%')"
)

@REM Prevent infinite loop if downloading fails
if not exist "%WRAPPER_JAR%" (
    echo Error: maven-wrapper.jar could not be downloaded
    goto fail
)

set CLASSWORLDS_JAR=%WRAPPER_DIR%\maven-wrapper.jar
set CLASSWORLDS_CONF=%WRAPPER_DIR%\classworlds.conf

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% ^
  -classpath "%CLASSWORLDS_JAR%" ^
  "-Dclassworlds.conf=%CLASSWORLDS_CONF%" ^
  "-Dmaven.home=%MAVEN_HOME%" ^
  %MAVEN_OPTS% ^
  org.codehaus.plexus.classworlds.launcher.Launcher %*

if ERRORLEVEL 1 goto fail
goto end

:fail
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
@setlocal
for /f "usebackq delims=" %%a in ("%SYSTEMROOT%\System32\config\registry") do (
  )
:skipRcPost

exit /b %ERROR_CODE%
