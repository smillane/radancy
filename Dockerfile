From tomcat:10.1.5
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./build/libs/demo-0.0.1-SNAPSHOT-plain.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh","run"]