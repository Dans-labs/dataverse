DataverseEU Docker image
===============
# Install docker compose suitable for your OS
https://docs.docker.com/compose/install/
 
git clone https://github.com/Dans-labs/dataverse

cd ./dataverse

git checkout 4.8.5-compose

cd ./conf/docker-eu
 
# download required software for authomatic Dataverse installation (PostgreSQL, SOLR, Glassfish, Dataverse)
./initial.bash
# build all containers with Docker Compose
docker-compose build
# Run images
docker-compose up
# Check if Dataverse is running
docker ps
# If you want to run specific version of Dataverse, run containers separately, for example, for French
docker-compose up dataverse_fr
 
After 15 minutes or so you’ll get localized Dataverses running on localhost:8085, localhost:8086 etc (see specification in .yaml file)
