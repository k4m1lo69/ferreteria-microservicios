# Ferretería ViSol - Sistema de Microservicios

Sistema de gestión para ferretería desarrollado con arquitectura de microservicios usando Spring Boot 3.4.5 y Java 21.

## Estudiante
- Camilo Contreras

## Microservicios implementados

| Servicio | Puerto | Base de datos |
|---|---|---|
| ms-auth | 8080 | db_auth |
| ms-producto | 8081 | db_productos |
| ms-pedidos | 8082 | db_pedidos |
| ms-clientes | 8083 | db_clientes |
| ms-pagos | 8084 | db_pagos |
| ms-reportes | 8085 | db_reportes |
| ms-inventarios | 8086 | db_inventarios |
| api-gateway | 8090 | — |

## Rutas del Gateway

Todas las peticiones se centralizan en `http://localhost:8090`

| Ruta | Microservicio destino |
|---|---|
| /api/auth/** | ms-auth (8080) |
| /api/productos/** | ms-producto (8081) |
| /api/pedidos/** | ms-pedidos (8082) |
| /api/clientes/** | ms-clientes (8083) |
| /api/pagos/** | ms-pagos (8084) |
| /api/reportes/** | ms-reportes (8085) |
| /api/inventarios/** | ms-inventarios (8086) |

## Documentación Swagger

Una vez levantado cada servicio, la documentación está disponible en:

- ms-auth: http://localhost:8080/swagger-ui/index.html
- ms-producto: http://localhost:8081/swagger-ui/index.html
- ms-pedidos: http://localhost:8082/swagger-ui/index.html
- ms-clientes: http://localhost:8083/swagger-ui/index.html
- ms-pagos: http://localhost:8084/swagger-ui/index.html
- ms-reportes: http://localhost:8085/swagger-ui/index.html
- ms-inventarios: http://localhost:8086/swagger-ui/index.html

## Instrucciones de ejecución local

### Requisitos
- Java 21
- Maven
- MySQL (Laragon recomendado)

### Pasos
1. Crear las bases de datos en MySQL:
```sql
CREATE DATABASE db_auth;
CREATE DATABASE db_productos;
CREATE DATABASE db_pedidos;
CREATE DATABASE db_clientes;
CREATE DATABASE db_pagos;
CREATE DATABASE db_reportes;
CREATE DATABASE db_inventarios;
```

2. Levantar cada microservicio desde su carpeta:
```bash
mvn spring-boot:run
```

3. Levantar el API Gateway:
```bash
cd api-gateway/api-gateway
mvn spring-boot:run
```

4. Acceder al Gateway en: `http://localhost:8090`

## Tecnologías utilizadas
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring Data JPA
- Spring Cloud Gateway
- Swagger/OpenAPI (springdoc)
- MySQL
- Lombok
- JUnit 5 + Mockito