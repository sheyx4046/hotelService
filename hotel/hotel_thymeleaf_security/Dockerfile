FROM openjdk
EXPOSE 8082
ARG JAR_FILE=build/libs/hotel_thymeleaf_security-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} villa-rent-docker

ENTRYPOINT ["java", "-jar", "villa-rent-docker"]