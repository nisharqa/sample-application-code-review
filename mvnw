#!/bin/sh
#
# Copyright 2015-2021 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Maven start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Maven Wrapper"
APP_JAR="maven-wrapper.jar"
APP_LSS="maven-wrapper.jar.sha256"
APP_HOME_RELATIVE=".mvn"
WRAPPER_MODULE_HOME="$APP_HOME/$APP_HOME_RELATIVE"
WRAPPER_JAR="$WRAPPER_MODULE_HOME/$APP_JAR"
WRAPPER_LSS="$WRAPPER_MODULE_HOME/$APP_LSS"

# Detect the Java command to use in order to perform the version check.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        echo "Error: JAVA_HOME is set to an invalid directory." >&2
        echo "JAVA_HOME = $JAVA_HOME" >&2
        exit 1
    fi
else
    echo "Error: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
    echo "Error: JAVA_HOME environment variable is not set."
    exit 1
fi

CLASSWORLDS_LAUNCHER="org.codehaus.plexus.classworlds.launcher.Launcher"

# traverses directory structure from process work directory to filesystem root
# first directory with .mvn subdirectory is considered project base directory
find_maven_basedir() {
    if [ -z "$1" ]
    then
        echo "Error: find_maven_basedir() received invalid file specification to search from." >&2
        return 1
    fi

    basedir="$1"
    wdir="$1"
    while [ "$wdir" != '/' ] ; do
        if [ -d "$wdir"/.mvn ] ; then
            basedir=$wdir
            break
        fi
        # workaround for JBEAP-8081 (on Solaris 10/Sparc)
        if [ -d "${wdir}" ]; then
            wdir=`cd "$wdir/.."; pwd`
        fi
        # end of workaround
    done
    echo "$basedir"
}

BASE_DIR=`find_maven_basedir "$(pwd)"`
if [ -z "$BASE_DIR" ] ; then
    exit 1
fi

##############################################################################
##
##  MavenWrapper Runner
##
##  MavenWrapper is a project-specific wrapper around a Maven installation.
##  By default, it will try to download Maven from Central Repository in the
##  .mvn/wrapper directory. Users can configure the Maven wrapper by changing
##  the POM or by specifying command line switches.
##
##############################################################################

MAVEN_OPTS="$(concat_lines "$MAVEN_BASE_ENV_VARS") $MAVEN_OPTS"

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
    [ -n "$M2_HOME" ] &&
        M2_HOME=`cygpath --path --windows "$M2_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    [ -n "$CLASSPATH" ] &&
        CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

# Provide a "standardized" way to retrieve the CLI args that will
# work with both Windows and non-Windows executions.
MAVEN_CMD_LINE_ARGS="$@"
export MAVEN_CMD_LINE_ARGS

while IFS="=" read -r key value
do
    case "$key" in (java.source|java.target) continue ;; esac
    case "$value" in
        *\'*)  Mary="${value//\'/\'\"\'\"\'}";;
        *)     Mary="'$value'";;
    esac
    MAVEN_OPTS="$MAVEN_OPTS -D$key=$Mary"
done < <(env | grep "^MAVEN_" || [ -z '' ])

exec "$JAVACMD" \
    -classpath "$WRAPPER_JAR" \
    "-Dclassworlds.conf=$WRAPPER_MODULE_HOME/classworlds.conf" \
    "-Dmaven.home=$MAVEN_HOME" \
    ${MAVEN_OPTS} \
    org.codehaus.plexus.classworlds.launcher.Launcher \
    "$@"
