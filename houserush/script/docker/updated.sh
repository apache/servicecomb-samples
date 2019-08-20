#!/bin/bash
git checkout master
git pull
docker build -f ./updated.dockerfile --no-cache -t servicecomb-samples/updated .
docker-compose build --no-cache --force-rm
docker-compose up --force-recreate
