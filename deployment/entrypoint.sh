#!/bin/bash
set -e

service neo4j-service stop
cd $CATALINA_HOME/bin/ && ./catalina.sh run