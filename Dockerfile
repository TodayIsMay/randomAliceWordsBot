FROM amazoncorretto:17
COPY target/RandomAliceWords-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
ENTRYPOINT ["java","-jar","/app.jar"]