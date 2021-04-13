#!/bin/bash

docker-compose -f docker/docker-compose-kafka.yml down
# delete all topics and messages
sudo rm -rf docker/kafka-logs
docker-compose -f docker/docker-compose-kafka.yml up -d