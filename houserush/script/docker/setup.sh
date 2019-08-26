#!/bin/bash
docker build -f base.dockerfile -t servicecomb-samples/base .
docker build -f mysql.dockerfile -t servicecomb-samples/mysql ../database

