FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6 AS build
WORKDIR /app
RUN apk update && apk add --no-cache maven bash
COPY pom.xml pom.xml
COPY orders/pom.xml orders/pom.xml
COPY orders/src orders/src
RUN mvn clean package -DskipTests -pl orders -am
FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6
WORKDIR /app
COPY --from=build /app/orders/target/ms-orders-1.0-SNAPSHOT.jar /app/ms-orders.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/ms-orders.jar"]