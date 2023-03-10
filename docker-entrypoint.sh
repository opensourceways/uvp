#!/usr/bin/env bash

set -e

WORKSPACE=/opt/uvp

cd ${WORKSPACE}
set +e
git pull
git submodule update --recursive

set -e
if [ -f "${DB_PASSWORD_FILE}" ]; then
  DB_PASSWORD=$(cat "${DB_PASSWORD_FILE}")
  export DB_PASSWORD
fi

if [ -f "${ENCRYPTION_PASSWORD_FILE}" ]; then
  ENCRYPTION_PASSWORD=$(cat "${ENCRYPTION_PASSWORD_FILE}")
  export ENCRYPTION_PASSWORD
fi

/bin/bash gradlew bootWar

java -jar ${WORKSPACE}/build/libs/uvp-1.0-SNAPSHOT.war --spring.profiles.active=prod
