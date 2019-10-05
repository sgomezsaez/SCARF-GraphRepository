#!/bin/bash
set -e

service neo4j-service stop
#service neo4j-service start
cd $CATALINA_HOME/bin/ && ./catalina.sh run