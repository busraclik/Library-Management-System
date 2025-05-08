# Base image olarak Java 21'i seçiyoruz
FROM openjdk:21-jdk-slim

# Çalışma dizinini belirliyoruz
WORKDIR /app

# Spring Boot jar dosyasını konteynıra kopyalıyoruz
COPY target/library-0.0.1-SNAPSHOT.jar app.jar

# Uygulama için portu expose ediyoruz (Spring Boot varsayılan olarak 8080 portunu kullanır)
EXPOSE 8080

# Uygulamayı çalıştırıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]
