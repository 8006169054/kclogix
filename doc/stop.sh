#!/bin/bash
APPLICATION_JAR_NAME=kc.logix.war

PS=`ps -ef|grep java|grep ${APPLICATION_JAR_NAME}`
PID=`ps -ef|grep java|grep ${APPLICATION_JAR_NAME}|awk '{print $2}'`

# Process Check
if [ "$PID" = "" ] ; then
	echo "####################################################"
	echo "${APPLICATION_JAR_NAME}"_Process is not Running!
	echo "####################################################"
	exit
fi

# Kainos core stop
kill -15 ${PID}
echo "#################################"
echo "##### shutdown #####"
echo ${PS}
echo "#################################"
