
FROM aomeri/fineract-base:latest AS WarBuilder

MAINTAINER  Antony Omeri, antonyomeri@gmail.com

RUN gradle -Penv=dev clean dist

FROM tomcat:jre8

ADD ./server.xml /usr/local/tomcat/conf/server.xml
ADD ./entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

RUN apt-get update \
    && apt-get install -y mysql-client \
    && apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN mkdir -p /run/mysqld \
    && chown 999 /run/mysqld

COPY --from=WarBuilder /src/build/libs/fineract-provider.war /usr/local/tomcat/webapps/
COPY fineract-db/mifospltaform-tenants-first-time-install.sql /docker-entrypoint-initdb.d/
RUN mkdir -p /docker-entrypoint-initdb.d \
    && sed -i '1s/^/USE mifosplatform-tenants;\n/' /docker-entrypoint-initdb.d/mifospltaform-tenants-first-time-install.sql \
    && wget http://central.maven.org/maven2/org/drizzle/jdbc/drizzle-jdbc/1.3/drizzle-jdbc-1.3.jar \
    && mv drizzle-jdbc-1.3.jar /usr/local/tomcat/lib \
    && cd / 

VOLUME /docker-entrypoint-initdb.d
ENTRYPOINT /entrypoint.sh
