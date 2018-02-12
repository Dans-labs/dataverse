#!/bin/sh

# move things necessary for integration tests into build context.
# this was based off the phoenix deployment; and is likely uglier and bulkier than necessary in a perfect world

mkdir -p testdata/doc/sphinx-guides/source/_static/util/
cp ../solr/4.6.0/schema.xml testdata/
cp ../../jhove/jhove.conf testdata/
#cd ../../../
#cp -r scripts conf/docker-aio/testdata/
#cp doc/sphinx-guides/source/_static/util/pg8-createsequence-prep.sql conf/docker-aio/testdata/doc/sphinx-guides/source/_static/util/
#cp doc/sphinx-guides/source/_static/util/createsequence.sql conf/docker-aio/testdata/doc/sphinx-guides/source/_static/util/

# not using dvinstall.zip for setupIT.bash; but still used in install.bash for normal ops
wdir=`pwd`
wdir=$wdir"/dataverse"
cd ./dataverse
echo $wdir
cd $wdir
if [ ! -e $wdir/dv/deps/dvinstall.zip ]; then
	wget https://github.com/IQSS/dataverse/releases/download/v4.8.5/dvinstall.zip -O $wdir/dv/deps/dvinstall.zip
fi

if [ ! -e $wdir/dv/deps/dataverse.war ]; then
 	wget https://github.com/IQSS/dataverse/releases/download/v4.8.5/dataverse-4.8.5.war -O $wdir/dv/deps/dataverse.war
fi
