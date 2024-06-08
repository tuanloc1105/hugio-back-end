#!/bin/bash

clear

ROOT_PROJECT_DIRECTORY=$(pwd)

cd common/

. ./build.sh

cd ${ROOT_PROJECT_DIRECTORY}

echo -e '\n\n >> Build maven \n\n'

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml
