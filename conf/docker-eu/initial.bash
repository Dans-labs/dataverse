#!/bin/bash

mkdir data
mkdir data/db
mkdir data/solr
#docker-compose build postgres
docker-compose start postgres
#docker-compose build solr
docker-compose start solr
