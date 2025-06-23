#!/bin/sh
APPLICATION_JAR_NAME=kc.logix.war

# Process Check
PID=`ps -ef|grep java|grep ${APPLICATION_JAR_NAME}|awk '{print $2}'`
if [ "$PID" != "" ] ; then
	echo "####################################################"
	echo "${APPLICATION_JAR_NAME}"_[pid:"${PID}"] Process is Running !
	echo "####################################################"
	exit
fi

# Kainos core start
nohup /apps/java/openlogic-openjdk-17.0.14+7-linux-x64/bin/java -jar ${APPLICATION_JAR_NAME} > /dev/null 2> /dev/null < /dev/null &
echo "#################################"
echo "###### start #######"
echo `ps -ef|grep java|grep ${APPLICATION_JAR_NAME}`
echo "#################################"
