# Base image
FROM openjdk:21-jdk-slim

# Çalışma dizinini
WORKDIR /app

# Spring Boot jar dosyasını konteynıra kopyala
COPY target/library-0.0.1-SNAPSHOT.jar app.jar

# Uygulama için portu expose ediyoruz
EXPOSE 8080

# Uygulamayı çalıştırıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]
