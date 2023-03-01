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

if [ -f "${OSSINDEX_API_TOKEN_FILE}" ]; then
  OSSINDEX_API_TOKENS=$(cat "${OSSINDEX_API_TOKEN_FILE}")
  export OSSINDEX_API_TOKENS
fi

if [ -f "${NVD_API_TOKEN_FILE}" ]; then
  NVD_API_TOKEN=$(cat "${NVD_API_TOKEN_FILE}")
  export NVD_API_TOKEN
fi

/bin/bash gradlew bootWar

java -jar ${WORKSPACE}/build/libs/uvp-1.0-SNAPSHOT.war --spring.profiles.active=prod
