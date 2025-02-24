FROM gradle:8.11.1-jdk21 AS build

WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle/
COPY settings.gradle build.gradle ./

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY . /app

RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/auth.jar


EXPOSE 8350
CMD ["java","-jar","/app/auth.jar"]