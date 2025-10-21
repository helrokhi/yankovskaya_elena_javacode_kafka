FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6 AS build
WORKDIR /app
RUN apk update && apk add --no-cache maven bash
COPY pom.xml pom.xml
COPY payment/pom.xml payment/pom.xml
COPY payment/src payment/src
RUN mvn clean package -DskipTests -pl payment -am
FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6
WORKDIR /app
COPY --from=build /app/payment/target/ms-payment-1.0-SNAPSHOT.jar /app/ms-payment.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/ms-payment.jar"]