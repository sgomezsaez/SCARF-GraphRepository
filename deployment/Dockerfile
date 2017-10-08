FROM tomcat:7
MAINTAINER Santiago Gomez <sgomezsaez@gmail.com>

RUN wget -O - https://debian.neo4j.org/neotechnology.gpg.key | apt-key add -
RUN echo 'deb http://debian.neo4j.org/repo stable/' > /tmp/neo4j.list
RUN mv /tmp/neo4j.list /etc/apt/sources.list.d
RUN apt-get update

RUN apt-get -y install neo4j=2.3.2
RUN chmod -R 777 /var/lib/neo4j/ && chmod -R 777 /etc/neo4j
RUN usermod -u 1000 neo4j
RUN service neo4j-service stop
RUN echo "org.neo4j.server.webserver.address=0.0.0.0" >> /var/lib/neo4j/conf/neo4j-server.properties
#ADD data/graph.db.tar.gz /var/lib/neo4j/data/
#RUN chmod -R 777 /var/lib/neo4j/data/graph.db

COPY tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY pertos.war /usr/local/tomcat/webapps/

EXPOSE 8080 7474 7687

WORKDIR /usr/local
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
CMD [ "/usr/local/entrypoint.sh" ]

