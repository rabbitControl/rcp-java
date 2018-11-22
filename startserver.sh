#!/bin/bash

PORT=10000;

if [ ! -z $2 ]; then
	PORT=$2;
fi

if [ -z $1 ]; then 
	./gradlew run;
else 
	./gradlew run -Dexec.args="-c $1 -p $PORT"; 
fi
