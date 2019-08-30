#!/bin/bash
docker build -f base.dockerfile --no-cache -t servicecomb-samples/base .
docker build -f mysql.dockerfile --no-cache -t servicecomb-samples/mysql ../database

