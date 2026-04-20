# Estágio 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache das dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e compila o JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta (o Render vai sobrescrever isso com a variável $PORT)
EXPOSE 8080

# Comando para rodar a aplicação usando a porta do Render
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]