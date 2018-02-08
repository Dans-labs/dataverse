#!/bin/bash

docker create volume dbstorage
docker-compose start postgres
docker create volume solrstorage
docker-compose start solr
