#!/bin/bash

if [ $# -eq 1 ]
  then
    java -jar weatherapp.jar $1
else
    java -jar weatherapp.jar
fi
