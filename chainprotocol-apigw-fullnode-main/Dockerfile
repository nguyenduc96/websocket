FROM maven:3.8.5-openjdk-11

WORKDIR /usr/src/
COPY . .
RUN mvn install:install-file -Dfile=spring-cloud-zuul-ratelimit-core-custom-2.4.3.RELEASE.jar -DgroupId=com.chainprotocol.gw -DartifactId=spring-cloud-zuul-ratelimit-core-custom -Dversion=2.4.3.RELEASE -Dpackaging=jar -DgeneratePom=true
RUN mvn package -Dmaven.test.skip

EXPOSE 8080
CMD [ "java", "-jar", "target/cp-gw-fullnode-1.0.0-SNAPSHOT.jar" ]
