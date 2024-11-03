# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que la aplicación va a correr
EXPOSE 8090

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]

