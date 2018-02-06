#!/usr/bin/env bash

# do integration-test install and test data setup

sudo -u postgres createuser dvnapp -h postgres
wget https://github.com/IQSS/dataverse/releases/download/v4.8.5/dvinstall.zip -O /opt/dv/dvinstall.zip
cd /opt/dv
unzip dvinstall.zip
cd /opt/dv/testdata
./scripts/deploy/phoenix.dataverse.org/prep
./db.sh
./install # modified from phoenix
/usr/local/glassfish4/glassfish/bin/asadmin deploy /opt/dv/dvinstall/dataverse.war
./post # modified from phoenix

