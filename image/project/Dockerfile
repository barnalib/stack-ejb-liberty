# Step 1: Build the user's application
FROM kabanero/ubi8-maven:0.9.0 as compile

RUN  groupadd java_group \
   && useradd --gid java_group --shell /bin/bash --create-home java_user \
   && mkdir -p /mvn/repository \
   && chown -R java_user:java_group /mvn \
   && mkdir -p /config \
   && chown -R java_user:java_group /config \
# make a well known place for shared library jars seperate from the rest of the defaultServer contents (to help with caching)
   && mkdir /configlibdir \
   && chown -R java_user:java_group /configlibdir \
   &&  mkdir /shared \
   && chown -R java_user:java_group /shared 

 USER java_user

COPY . /project

WORKDIR /project/user-app

# Copy and build the dev.appsody:java-openliberty parent pom
COPY --chown=java_user:java_group ./pom.xml /project/pom.xml
RUN cd /project && mvn -B install dependency:go-offline -DskipTests

COPY --chown=java_user:java_group ./user-app /project/user-app
RUN cd /project/user-app && \ 
mvn clean package	
RUN find /project/user-app -name '*.[e]ar' -exec cp -prv '{}' '/project/user-app/liberty/' ';'  
FROM openliberty/open-liberty:kernel-java8-openj9-ubi
 
ARG SSL=false
ARG MP_MONITORING=false
ARG HTTP_ENDPOINT=false
 

COPY --chown=1001:0 ./user-app/liberty/server.xml /liberty/usr/servers/defaultServer/server.xml
COPY --chown=1001:0 ./user-app/liberty/jvm.options /liberty/usr/servers/defaultServer/jvm.options
COPY --chown=1001:0 ./user-app/liberty/server.env /liberty/usr/servers/defaultServer/server.env
COPY --chown=1001:0 --from=compile /project/user-app/liberty/*.[ewj]ar /liberty/usr/servers/defaultServer/apps/
COPY --chown=1001:0 ./user-app/resources/ /liberty/usr/shared/resources/ 
# COPY --chown=1001:0 ./user-app/liberty/server.xml /opt/ol/wlp/usr/servers/defaultServer/server.xml
# COPY --chown=1001:0 ./user-app/liberty/server.xml /opt/ol/wlp/usr/servers/defaultServer/jvm.options
# COPY --chown=1001:0 --from=compile /project/user-app/liberty/*.[ewj]ar /config/apps
# COPY --chown=1001:0 --from=compile /project/user-app/CustomerOrderServicesApp/target/CustomerOrderServicesApp-0.1.0-SNAPSHOT.ear /liberty/usr/servers/defaultServer/apps/CustomerOrderServicesApp-0.1.0-SNAPSHOT.ear 
# COPY --chown=1001:0 ./user-app/resources/ /opt/ol/wlp/usr/shared/resources/

USER root
RUN configure.sh
USER 1001

# Set metadata
ARG artifactId=ejb-liberty
ARG version=1.0-SNAPSHOT
ENV JVM_ARGS=""

LABEL org.opencontainers.image.version="${version}"
LABEL org.opencontainers.image.title="${artifactId}"
LABEL appsody.stack="{{.stack.image.namespace}}/{{.stack.id}}:{{.stack.semver.major}}.{{.stack.semver.minor}}.{{.stack.semver.patch}}"

EXPOSE 9080
EXPOSE 9443
