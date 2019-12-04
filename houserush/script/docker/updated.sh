#!/bin/bash
git checkout master
git pull
docker build -f ./updated.dockerfile --no-cache -t servicecomb-samples/updated .
docker-compose rm -f
docker-compose build --no-cache --force-rm
if [ ! -n $1 ]; then
  docker-compose up --force-recreate
fi
