# Menggunakan base image OpenJDK 17 yang ringan (sesuai rules)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory di dalam container
WORKDIR /app

# Copy file jar hasil build ke dalam container
# (Pastikan Anda sudah menjalankan 'mvn clean package' atau via IDE)
COPY target/ticketing-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port yang digunakan aplikasi
EXPOSE 8080

# Jalankan aplikasi Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
