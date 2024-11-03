FROM ubuntu:latest
LABEL authors="Nicolas Morales"

ENTRYPOINT ["top", "-b"]

# Usar una imagen base de Java
FROM openjdk:17-jre-slim

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar el archivo .jar al contenedor
COPY build/libs/tu-aplicacion.jar app.jar

# Exponer el puerto que usa la aplicación
EXPOSE 8090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]