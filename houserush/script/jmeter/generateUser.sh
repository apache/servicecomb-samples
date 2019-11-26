#!/bin/bash
i=$1
e=$2
while [ $i -le $e ]
do  
  echo "customer"$i",123456"
  let "i++"
done
