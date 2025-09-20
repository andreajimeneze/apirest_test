# API Testing con Spring Boot y Rest Assured

Este proyecto permite probar endpoints REST usando **Spring Boot**, **Maven** y **Rest Assured**.


## 1. Requisitos

- Java 11 o superior
- Maven 3.6+
- IDE recomendado: IntelliJ IDEA, Eclipse o VS Code
- API corriendo localmente (ej. `http://localhost:8081/api/v1`)

---
## Comando ejecución test

- mvn clean test

## 2. Estructura de carpetas

apirestAssured/
├── postman/
│   ├── newman/
│   │   ├── APIEnvironment.postman_environment.json
│   │   └── ApirestModular6.postman_collection.json
│   └── .env
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cl/kibernum/apirest/
│   │   │       ├── ApirestApplication.java
│   │   │       ├── controllers/
│   │   │       │   └── ProductoController.java
│   │   │       ├── dto/
│   │   │       │   └── ProductoDto.java
│   │   │       ├── entities/
│   │   │       │   └── Producto.java
│   │   │       ├── exception/
│   │   │       │   ├── ResourceDuplicateException.java
│   │   │       │   └── ResourceNotFoundException.java
│   │   │       ├── repositories/
│   │   │       │   └── ProductoRepository.java
│   │   │       └── services/
│   │   │           ├── ICrudService.java
│   │   │           ├── IProductoService.java
│   │   │           └── ProductoServiceImpl.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── data.sql
│   │       ├── META-INF/
│   │       │   └── additional-spring-configuration-metadata.json
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── cl/kibernum/apirest/
│               ├── ApirestApplicationTests.java
│               ├── config/
│               │   ├── Env.java
│               │   └── Specs.java
│               ├── controllers/
│               │   └── ProductoCliente.java
│               ├── dto/
│               │   └── .gitkeep
│               ├── entities/
│               │   └── .gitkeep
│               ├── exception/
│               │   └── .gitkeep
│               ├── repositories/
│               │   └── .gitkeep
│               ├── services/
│               │   └── .gitkeep
│               └── test/
│                   └── ProductoCrudTest.java
├── target/
│   ├── classes/
│   │   └── cl/kibernum/apirest/  (archivos compilados .class)
│   ├── site/jacoco/  (reportes de cobertura)
│   └── surefire-reports/  (reportes de tests)
└── pom.xml
