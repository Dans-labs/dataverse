#!/usr/bin/env bash

cd /opt/glassfish4
bin/asadmin stop-domain
bin/asadmin start-domain
sleep infinity

