FROM centos:7
# OS dependencies
RUN yum install -y java-1.8.0-openjdk-headless sudo epel-release unzip perl curl
RUN yum install -y wget patch postgresql lynx jq

# copy and unpack dependencies (solr, glassfish)
COPY lang.properties /opt/dv
WORKDIR /opt/dv
COPY dv /opt/dv

COPY testdata/schema.xml /top/dv
#RUN cd /opt ; tar zxf /opt/dv/deps/glassfish4dv.tgz

# copy configuration related files
#RUN cp /tmp/dv/pg_hba.conf /var/lib/pgsql/data/ ; cp /tmp/dv/schema.xml /opt/solr-4.6.0/example/solr/collection1/conf/schema.xml

# glassfish port
EXPOSE 8080

COPY dv/deps/dvinstall.zip /opt/dv
COPY docker.patch /opt/dv

# yeah - still not happy if glassfish isn't in /usr/local :<
RUN ln -s /opt/glassfish4 /usr/local/glassfish4
#COPY dv/install/ /opt/dv/
COPY install.bash /opt/dv/
COPY entrypoint.bash /opt/dv/
COPY testdata /opt/dv/testdata
COPY testscripts/* /opt/dv/testdata/
COPY setupIT.bash /opt/dv
#COPY dvinstall/dataverse.war /opt/dv
COPY lang.properties /opt/dv
#WORKDIR /opt/dv
#RUN bash setupIT.bash
#RUN bash install.bash 
RUN echo "Installing and running Dataverse..."
CMD ["/opt/dv/entrypoint.bash"]
