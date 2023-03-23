FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=./build/libs/app.jar
RUN pwd
RUN ls -alR
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
