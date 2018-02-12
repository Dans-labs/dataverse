DataverseEU Docker image
===============
# Install docker compose
https://docs.docker.com/compose/install/
 
git clone https://github.com/Dans-labs/dataverse

git checkout 4.8.5-compose

cd dataverse/conf/docker-eu
 
# download all software
./ initial.bash
# build all containers
./docker-compose build
# Run images
./docker-compose up
 
After 15 minutes or so you’ll get localized Dataverses running on localhost:8085, localhost:8086 etc (see specification in .yaml file)
