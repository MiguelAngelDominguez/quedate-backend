# quédate-backend

Backend de **Quédate** — plataforma de habitaciones verificadas para becarios UTEC (campus Miraflores).

## Stack

- Java 26 + Spring Boot 4.1.x (Maven)
- Spring Web, Data JPA, Security (JWT), Validation, Mail (Thymeleaf), Actuator
- PostgreSQL (RDS en AWS) · H2 en desarrollo
- Lombok + ModelMapper

## Estructura

- `PLAN_ESTRUCTURAL.md` — plan completo del proyecto, reparto por integrante, rutas de archivos y rúbrica (20 pts).
- `README.md` — este archivo (versión final para entrega se genera en el milestone M5).

## Integrantes

| Rol | GitHub | Área |
|---|---|---|
| A | @MiguelAngelDominguez | Fundación, seguridad y usuarios |
| B | @andremejia-hub | Catálogo y búsqueda de habitaciones |
| C | @iygt8-iterate | Confianza, solicitudes y comunicación |

## Ejecutar

```bash
mvn spring-boot:run
```

Abre el proyecto en IntelliJ IDEA (File → Open → selector de `pom.xml`).