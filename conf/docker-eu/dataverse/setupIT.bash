#!/usr/bin/env bash

# do integration-test install and test data setup

sudo -u postgres createuser dvnapp -h postgres
#wget https://github.com/IQSS/dataverse/releases/download/v4.8.5/dvinstall.zip -O /opt/dv/dvinstall.zip
cd /opt/dv
#mv deps/dvinstall.zip ./
rm -rf dvinstall
unzip dvinstall.zip
#cd /opt/dv/testdata
#./scripts/deploy/phoenix.dataverse.org/prep
#./db.sh
#./install # modified from phoenix
patch -t /opt/dv/dvinstall/install < docker.patch
cd /opt/dv/dvinstall
/usr/local/glassfish4/glassfish/bin/asadmin start-domain
./install -admin_email=pameyer+dvinstall@crystal.harvard.edu -y -f 
#> install.out 2> install.err

cd /opt/dv/deps
ls -la
#/usr/local/glassfish4/glassfish/bin/asadmin deploy /opt/dv/deps/dataverse.war
#./post # modified from phoenix
echo "Applying language properties..."
/usr/local/glassfish4/glassfish/bin/asadmin stop-domain
sleep 10s
cp -rf /opt/dv/$BUNDLEPROPERTIES /opt/glassfish4/glassfish/domains/domain1/applications/dataverse/WEB-INF/classes/Bundle.properties
/usr/local/glassfish4/glassfish/bin/asadmin start-domain
