#!/bin/bash

JAVAENV="-J-server -J-Xms256m -J-Xmx1G -Dconfig.resource=application.conf -Dlogger.resource=logback.xml"

PORT="80"
if ! [ -z "$2" ]
  then
    PORT="$2"
fi

JAVAENV="$JAVAENV -Dhttp.port=$PORT"

SERVICE="vtagadmin-$PORT"
PIDFILE="$SERVICE.pid"

function start {
   if [ -f $PIDFILE ]; then
      pid="$(<$PIDFILE)"
      if ps ax | grep -v grep | grep $pid > /dev/null
      then
         echo "$SERVICE service running ($pid), need stop before start"
         return 1;
      fi
   fi
   rm -rf vtagadmin-1.0-SNAPSHOT/RUNNING_PID
   javaCmd="nohup vtagadmin-1.0-SNAPSHOT/bin/vtagadmin $JAVAENV > /dev/null 2>&1 & echo \$! >$PIDFILE"
   eval "$javaCmd"
   pid="$(<$PIDFILE)"
   echo "$SERVICE service started ($pid)"
   return 0; }


function stop {
   pid="$(<$PIDFILE)"
   kill -s SIGTERM $pid || return 1
   echo "$SERVICE service stopped"
   rm -f "$PIDFILE"
   return 0; }

function restart {
   stop
   start
   return 0; }

function status {
   if [ -f $PIDFILE ]; then
      pid="$(<$PIDFILE)"
      ps ax | grep -v grep | grep $pid
   else
      ps ax | grep -v grep | grep localhost
   fi
   return 0; }

function main {
   RETVAL=0
   case "$1" in
      start)
         start
         ;;
      stop)
         stop
         ;;
      restart)
         restart
         ;;
      status)
         status
         ;;
      *)
         echo "Usage: $0 {start|stop}"
         exit 1
         ;;
      esac
   exit $RETVAL
}


main $1
