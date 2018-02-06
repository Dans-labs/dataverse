FROM centos:7
# OS dependencies
RUN yum install -y java-1.8.0-openjdk-headless postgresql-server sudo epel-release unzip perl curl
RUN yum install -y wget patch postgresql lynx jq

# copy and unpack dependencies (solr, glassfish)
COPY dv /tmp/dv
COPY testdata/schema.xml /tmp/dv
#RUN cd /opt ; tar zxf /tmp/dv/deps/solr-4.6.0dv.tgz 
RUN cd /opt ; tar zxf /tmp/dv/deps/glassfish4dv.tgz

#RUN sudo -u postgres /usr/bin/initdb -D /var/lib/pgsql/data
#RUN sudo -u postgres createuser dvnapp -h postgres 
#RUN /usr/bin/createuser -U postgres dvnapp -h postgres

# copy configuration related files
#RUN cp /tmp/dv/pg_hba.conf /var/lib/pgsql/data/ ; cp /tmp/dv/schema.xml /opt/solr-4.6.0/example/solr/collection1/conf/schema.xml

# skipping glassfish user and solr user (run both as root)

#solr port
#EXPOSE 8983

# postgres port
#EXPOSE 5432

# glassfish port
EXPOSE 8080

#RUN rm /opt/dv
RUN mkdir /opt/dv
COPY dvinstall.zip /opt/dv
COPY docker.patch /opt/dv


# yeah - still not happy if glassfish isn't in /usr/local :<
RUN ln -s /opt/glassfish4 /usr/local/glassfish4
COPY dv/install/ /opt/dv/
COPY install.bash /opt/dv/
COPY entrypoint.bash /opt/dv/
COPY testdata /opt/dv/testdata
COPY testscripts/* /opt/dv/testdata/
COPY setupIT.bash /opt/dv
COPY dvinstall/dataverse.war /opt/dv
COPY lang.properties /opt/dv
WORKDIR /opt/dv
#RUN bash setupIT.bash
CMD ["/opt/dv/entrypoint.bash"]
