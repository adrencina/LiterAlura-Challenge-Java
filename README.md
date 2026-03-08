# LiterAlura - Challenge ONE Java

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

**LiterAlura** es una aplicación de consola desarrollada como parte del Challenge de Alura Latam y Oracle Next Education (ONE). El objetivo principal es construir un catálogo de libros interactivo que consume datos de la API [Gutendex](https://gutendex.com/), realiza el mapeo de los mismos y los persiste en una base de datos relacional para su posterior consulta y análisis.

## 🚀 Funcionalidades

La aplicación ofrece un menú interactivo con las siguientes opciones:

1.  **Buscar libro por título**: Realiza una petición a la API Gutendex y registra el libro y su autor en la base de datos (evitando duplicados).
2.  **Listar libros registrados**: Muestra todos los libros que han sido guardados en la base de datos.
3.  **Listar autores registrados**: Muestra una lista de todos los autores cuyos libros han sido buscados.
4.  **Listar autores vivos en un determinado año**: Filtra los autores registrados que estaban vivos en el año ingresado por el usuario.
5.  **Listar libros por idioma**: Permite filtrar los libros de la base de datos por su idioma (es, en, fr, pt).

## 🛠️ Tecnologías Utilizadas

-   **Java 17**: Lenguaje de programación principal.
-   **Spring Boot 3.2.3**: Framework para facilitar la configuración y el desarrollo.
-   **Spring Data JPA**: Para el mapeo objeto-relacional (ORM) y la persistencia de datos.
-   **PostgreSQL**: Sistema de gestión de base de datos relacional.
-   **Jackson**: Biblioteca para el procesamiento y mapeo de datos JSON.
-   **HttpClient**: Para el consumo de la API externa.

## 📋 Requisitos Previos

-   Java JDK 17 o superior.
-   Maven instalado (o usar el wrapper `./mvnw` incluido).
-   PostgreSQL instalado y configurado con una base de datos llamada `literalura_db`.

## ⚙️ Configuración

Asegúrate de configurar las credenciales de tu base de datos en el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

## 🏃 Ejecución

Clona el repositorio y ejecuta el siguiente comando en la terminal:

```bash
./mvnw spring-boot:run
```

---
**Encina Adrián** - *Desarrollador de Software*
