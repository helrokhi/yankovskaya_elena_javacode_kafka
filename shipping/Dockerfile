FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6 AS build
WORKDIR /app
RUN apk update && apk add --no-cache maven bash
COPY pom.xml pom.xml
COPY shipping/pom.xml shipping/pom.xml
COPY shipping/src shipping/src
RUN mvn clean package -DskipTests -pl shipping -am
FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6
WORKDIR /app
COPY --from=build /app/payment/target/ms-shipping-1.0-SNAPSHOT.jar /app/ms-shipping.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/ms-shipping.jar"]