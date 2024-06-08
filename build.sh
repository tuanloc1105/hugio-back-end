#!/bin/bash

clear

ROOT_PROJECT_DIRECTORY=$(pwd)

cd common/

bash ./build.sh

cd ${ROOT_PROJECT_DIRECTORY}

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml
