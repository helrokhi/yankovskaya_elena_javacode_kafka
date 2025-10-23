FROM maven:3.9.9 AS builder

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
