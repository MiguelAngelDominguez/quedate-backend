# Quédate — Backend

**Curso:** CS 2031 Desarrollo Basado en Plataforma
**Universidad:** UTEC
**Ciclo:** 2026-2
**Deadline:** 25/09/2026 · **Objetivo:** 20/20 según rúbrica (ver `PLAN_ESTRUCTURAL.md`)

## Integrantes

| Área | GitHub | Responsabilidad |
|---|---|---|
| A | [@MiguelAngelDominguez](https://github.com/MiguelAngelDominguez) | Fundación, seguridad, autenticación y usuarios |
| B | [@andremejia-hub](https://github.com/andremejia-hub) | Catálogo, habitaciones, ubicaciones y búsqueda |
| C | [@iygt8-iterate](https://github.com/iygt8-iterate) | Confianza, solicitudes, visitas, reseñas, verificación, eventos y correo |

---

## Índice

1. Introducción
2. Identificación del problema
3. Descripción de la solución
4. Tecnologías utilizadas
5. Arquitectura del backend
6. Modelo de entidades
7. API REST
8. Manejo de errores
9. Seguridad y autenticación
10. Eventos, asincronía y correo
11. Ejecución del proyecto
12. Deployment
13. GitHub y gestión del proyecto
14. Conclusiones
15. Apéndices

---

## 1. Introducción

**Quédate** es una plataforma orientada a estudiantes universitarios que necesitan encontrar habitaciones cercanas a su universidad de manera organizada y confiable. Se enfoca principalmente en estudiantes de UTEC y concentra información que normalmente vive dispersa en redes sociales, grupos informales o contactos personales.

El backend expone una API REST versionada bajo `/api/v1` que permite gestionar usuarios, arrendadores, habitaciones, ubicaciones, solicitudes de alquiler, visitas, reseñas y verificación de arrendadores, con seguridad basada en JWT y notificaciones por correo.

---

## 2. Identificación del problema

Buscar alojamiento cerca de una universidad resulta complicado para un estudiante que no conoce la zona o debe trasladarse todos los días. Las ofertas suelen publicarse en medios generales o informales, lo que genera:

- Información fragmentada entre plataformas.
- Dificultad para comparar precios, ubicaciones y características.
- Falta de validación de arrendadores.
- Ausencia de un flujo estructurado para solicitar una habitación.
- Coordinación manual de visitas.
- Reseñas sin comprobar una experiencia real.
- Poca información sobre la cercanía a la universidad.

El alojamiento impacta directamente el tiempo de transporte, los gastos y la experiencia universitaria. Quédate centraliza la búsqueda y la interacción con el arrendador.

---

## 3. Descripción de la solución

El backend implementa módulos que cubren el ciclo completo de la propuesta:

- **Usuarios y roles:** registro, autenticación y perfiles de estudiante, arrendador y administrador.
- **Catálogo:** universidades, ubicaciones y habitaciones; búsqueda por universidad, distrito, rango de precio y ordenamiento, con cálculo de distancia (Haversine).
- **Habitaciones (landlord/admin):** CRUD completo, habitaciones propias y publicación/archivado.
- **Trust flow (confianza):** solicitudes de alquiler con estados, visitas, reseñas y calificaciones de arrendadores, y verificación manual de identidad por un administrador.
- **Comunicación:** eventos de dominio y correos transaccionales asíncronos.

Para poder reseñar, el estudiante debe haber tenido una experiencia verificable con la habitación (solicitud confirmada o visita completada), lo que reduce reseñas sin relación real con el alojamiento.

---

## 4. Tecnologías utilizadas

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 26 |
| Framework | Spring Boot 4.1.x |
| Build | Maven |
| Web | Spring Web (REST) |
| Persistencia | Spring Data JPA, Hibernate |
| Base de datos | PostgreSQL (prod) · H2 en memoria (dev) |
| Seguridad | Spring Security, JWT, BCrypt |
| Validación | Jakarta Validation |
| Productividad | Lombok, ModelMapper |
| Correo | JavaMailSender + Thymeleaf |
| Operación | Spring Actuator (health/info) |
| Contenedores (bonus) | Docker + Docker Compose |

Durante el desarrollo se usa el perfil `dev` con H2 en memoria (`create-drop` y consola `/h2-console`). Para entornos reales existe el perfil `prod` sobre PostgreSQL mediante variables de entorno.

---

## 5. Arquitectura del backend

Arquitectura por capas con inyección de dependencias por constructor:

```text
Controller  (HTTP, validación, DTO)
    ↓
Service     (reglas de negocio, transacciones, seguridad de dominio)
    ↓
Repository  (Spring Data JPA)
    ↓
Database
```

Los **controllers** son delgados; los **services** concentran las reglas (disponibilidad, ownership, transiciones de estado, duplicados, verificación de experiencias, eventos) y los **repositories** abstraen el acceso a datos. Los **DTO** separan las entidades persistidas de los contratos HTTP.

Paquetes principales (`com.quedate`):

```text
config/    controller/   dto/    email/   entity/
event/     exception/    repository/   security/   service/   util/
```

Este orden separa responsabilidades y permitió dividir el trabajo entre las tres personas del equipo sin colisiones.

---

## 6. Modelo de entidades

12 entidades JPA en `com.quedate.entity`:

| Entidad | Descripción |
|---|---|
| `User` / `Role` | Usuario, autenticación y roles (`USER`, `LANDLORD`, `ADMIN`) |
| `Student` / `Landlord` | Perfiles especializados del estudiante y el arrendador |
| `University` / `Location` | Universidades y ubicaciones (distrito, coordenadas, distancia) |
| `Room` | Habitación ofrecida (título, precio, capacidad, tamaño, imágenes, estado) |
| `Publication` | Publicación (activa/archivada) vinculada a una habitación |
| `RentalRequest` / `Visit` | Solicitud de alquiler y visita programada |
| `Review` / `Verification` | Reseña-calificación y verificación de identidad |

Las relaciones usan `@ManyToOne`, `@OneToOne` y `@ManyToMany`. Ejemplos: una solicitud pertenece a un estudiante y a una habitación; una visita pertenece a una solicitud; una reseña vincula estudiante-habitación con restricción única (una reseña por estudiante y habitación).

Los estados se modelan con enums: `RoomStatus`, `PublicationStatus`, `RentalRequestStatus`, `VisitStatus`, `VerificationStatus` y `VerificationType`. El diseño detallado está en `PLAN_ESTRUCTURAL.md`.

---

## 7. API REST

Prefijo base: `/api/v1`

### Autenticación y usuarios

```text
POST  /api/v1/auth/register              # crear cuenta (USER | LANDLORD)
POST  /api/v1/auth/login                 # JWT + refresh
POST  /api/v1/auth/refresh               # renovar access token
GET   /api/v1/users/me                   # perfil propio
PUT   /api/v1/users/me
GET   /api/v1/students/{id}
PUT   /api/v1/landlords/me/profile
GET   /api/v1/admin/users                # (admin)
```

### Catálogo

```text
GET    /api/v1/universities                    POST /api/v1/universities (admin)
GET    /api/v1/universities/{id}
GET    /api/v1/locations?district=             POST /api/v1/locations (admin)
GET    /api/v1/rooms                           # búsqueda: minPrice, maxPrice, district, universityId, page, size, sortBy
GET    /api/v1/rooms/{id}
GET    /api/v1/landlords/{id}/rooms            # habitaciones de un arrendador (público)
```

### Gestión de habitaciones (landlord/admin)

```text
POST   /api/v1/rooms                # crear habitación
PUT    /api/v1/rooms/{id}
DELETE /api/v1/rooms/{id}
GET    /api/v1/rooms/my-rooms       # habitaciones del arrendador autenticado
POST   /api/v1/my-rooms/{id}/publish
POST   /api/v1/my-rooms/{id}/archive
```

### Solicitudes de alquiler y visitas

```text
POST   /api/v1/rooms/{roomId}/rental-requests
GET    /api/v1/my-rental-requests             # del estudiante
GET    /api/v1/my-requests-landlord           # del arrendador
PATCH  /api/v1/rental-requests/{id}/status    # PENDING→CONFIRMED/REJECTED/CANCELLED
POST   /api/v1/rental-requests/{id}/visits    # agendar visita
GET    /api/v1/rental-requests/{id}/visits
PATCH  /api/v1/visits/{id}/status             # →CONFIRMED (landlord) · →COMPLETED/NO_SHOW (estudiante)
```

### Reseñas y rating

```text
POST  /api/v1/rooms/{roomId}/reviews
GET   /api/v1/rooms/{roomId}/reviews
GET   /api/v1/landlords/{landlordId}/rating   # promedio + conteo (público)
```

### Verificación

```text
POST   /api/v1/me/verification                # solicitar verificación (landlord)
GET    /api/v1/verifications/pending          # (admin)
PATCH  /api/v1/verifications/{id}             # APPROVED | REJECTED (admin)
```

Códigos de respuesta usados: `200`, `201`, `204`, `400`, `401`, `403`, `404`, `409` y `500`. La colección completa con variables y autenticación heredada está en `postman_collection.json`.

---

## 8. Manejo de errores

Excepciones de negocio en `com.quedate.exception` y manejo global con `@RestControllerAdvice` + `GlobalExceptionHandler`:

| Caso | Resultado |
|---|---|
| Recurso inexistente | `ResourceNotFoundException` → `404` |
| Recurso duplicado | `DuplicateResourceException` → `409` |
| Operación inválida / transición inválida | `InvalidOperationException`, `InvalidTransitionException` → `400` |
| Horario o estado de visita inválido | `InvalidScheduleException` → `400` |
| Sin permisos o sin perfil | `ForbiddenException` → `403` |
| Autenticación o token inválido | Excepciones de seguridad/JWT → `401` |
| Error no controlado | → `500` |

La respuesta de error usa el contrato `ErrorResponseDTO` (`timestamp`, `status`, `error`, `message`, `path`), evitando exponer excepciones internas de Java al cliente.

---

## 9. Seguridad y autenticación

Spring Security stateless con JWT:

```text
Authorization: Bearer <token>
```

- **Autenticación:** `AuthService` (register/login/refresh), `UserPrincipal` y `UserDetailsServiceImpl`.
- **JWT:** `JwtService` en `security/` genera y valida tokens con claims del usuario y sus roles; expiración de access y refresh configurable.
- **Contraseñas:** BCrypt; ningún DTO expone el hash.
- **Autorización:** `@PreAuthorize("hasRole('ADMIN')")` / `'USER'` / `'LANDLORD'`, más verificación de *ownership* por `user.id` en servicios (prohibido tocar recursos ajenos).
- **Rutas públicas:** `/api/v1/auth/**`, catálogo (`universities`, `locations`, `rooms`), `GET /api/v1/landlords/**` y `swagger-ui`.
- **CORS:** predeterminado a `http://localhost:3000`.

Secretos y credenciales se pasan por variables de entorno (`JWT_SECRET`, `DB_URL`, `SMTP_*`, `CORS_ALLOWED_ORIGINS`) documentadas en `.env.example`; nunca se almacenan en el repositorio.

---

## 10. Eventos, asincronía y correo

Eventos de dominio (`event/`) publicados por los servicios y escuchados con `@EventListener`:

- `UserRegisteredEvent` → plantilla `welcome.html`.
- `RentalRequestCreatedEvent` → `request-notification.html` (notifica al arrendador).
- `VisitScheduledEvent` → `visit-confirmation.html` (estudiante y arrendador).

`EmailService.sendHtml(to, subject, template, context)` usa JavaMailSender + Thymeleaf y es **`@Async`** (config en `AsyncConfig`), de modo que el envío no bloquea la respuesta de la API. La plantilla `reset-password.html` también está disponible. El flag `app.mail.enabled` permite desactivar el correo (p. ej., en desarrollo) sin romper la operación: simplemente se registra el evento y continúa el flujo.

---

## 11. Ejecución del proyecto

### Requisitos

- JDK 26 · Maven 3.9+ · Git · (opcional) Docker.

### Con Maven (perfil dev, H2 en memoria)

```bash
mvn spring-boot:run
```

La app arranca en `http://localhost:8080` (health en `/actuator/health`; consola H2 en `/h2-console`). El `DataSeeder` crea el admin (`admin@quedate.com` / `Admin123!`), un arrendador y habitaciones de ejemplo. Perfil `dev` activo por defecto.

### Con Docker Compose (bonus, perfil prod + PostgreSQL)

```bash
docker compose up --build
```

Levanta PostgreSQL 16 y la app en `http://localhost:8080`. El perfil `prod` requiere las variables de entorno del archivo `.env` (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, etc.).

### Desde IntelliJ IDEA

Abrir la carpeta raíz (detecta `pom.xml`), configurar SDK **Java 26**, permitir que Maven descargue dependencias y ejecutar `com.quedate.QuedateApplication`.

### Variables de entorno

Documentadas en `.env.example`: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `REFRESH_EXPIRATION`, `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `SMTP_FROM`, `MAIL_ENABLED`, `CORS_ALLOWED_ORIGINS`.

### Pruebas

```bash
mvn test   # 32 tests unitarios (Mockito) sobre servicios de dominio
```

---

## 12. Deployment

La arquitectura de producción contempla:

- **AWS EC2** para ejecutar el backend.
- **AWS RDS PostgreSQL** como base de datos.
- Variables de entorno para credenciales y secretos.
- Security Groups para controlar las conexiones permitidas.

### URL del backend desplegado

```text
[PENDIENTE: colocar la URL final correspondiente al issue #34]
```

La URL deberá actualizarse después de finalizar y verificar el deployment definitivo.

---

## 13. GitHub y gestión del proyecto

- **GitFlow híbrido:** cada issue deriva en una rama `feat/<integrante>/<tarea>` desde `develop` e integra mediante **Pull Request** con revisión. `main` se reserva para versiones estables.
- **Resultado:** 55 PRs mergeados en `develop` (48 de funcionalidad + fixes y documentación), todas las ramas conservadas.
- **Issues y milestones:** los hitos **M0–M5** organizan bootstrap, seguridad, catálogo, confianza, integración y docs/QA; los 39 issues tienen evidencia de cierre y los 6 milestones quedaron cerrados.
- **Verificación:** gates funcionales **M2 24/24** y **M3 24/24**, `mvn clean compile` OK y **32/32 tests**.
- **Documentación:** `PLAN_ESTRUCTURAL.md` (informe estructural + rúbrica), `FLUJO_DE_RECOMENDACION.md`, `docs/CHECKLIST_RUBRICA.md` y `docs/VERIFICACION_M2_M5.md`.
- **Entregables:** `README.md`, `postman_collection.json` (38 requests, auth heredada) y `Dockerfile`/`docker-compose.yml`.

La integración continua (CI) y el deploy automatizado quedan como trabajo futuro; en este entregable el control fue manual (PRs + gates + tests).

---

## 14. Conclusiones

Quédate resuelve un problema concreto: facilitar a los estudiantes la búsqueda y gestión de alojamiento cerca de su universidad. El backend integra autenticación y roles, catálogo con búsqueda y distancias, CRUD de habitaciones, publicaciones, solicitudes de alquiler con estado, visitas, reseñas con experiencia verificable, verificación de arrendadores, eventos de dominio y correo asíncrono.

Se aplicaron conceptos sólidos: arquitectura por capas, REST, Spring Data JPA, DTO, validación, excepciones globales, JWT y autorización por roles, transacciones, ownership checks y asincronía. El uso de ramas, issues y Pull Requests permitió que tres personas trabajaran en paralelo sobre módulos distintos.

Como trabajo futuro: imágenes en AWS S3, mayor cobertura de pruebas, CI/CD automatizado, monitoreo en producción, deploy en AWS (issue #34) y expansión a más universidades.

---

## 15. Apéndices

### Archivos principales

```text
README.md                     # este documento
PLAN_ESTRUCTURAL.md           # informe estructural y rúbrica 20/20
FLUJO_DE_RECOMENDACION.md     # guía de colaboración
docs/CHECKLIST_RUBRICA.md     # checklist final de la rúbrica
docs/VERIFICACION_M2_M5.md    # verificación M2–M5 (gates, bugs, build)
postman_collection.json       # colección completa (38 requests)
Dockerfile / docker-compose.yml
.env.example
pom.xml
src/main/resources/application.yml
src/main/resources/application-dev.yml     # H2 (dev)
src/main/resources/application-prod.yml    # PostgreSQL (prod)
```

### Licencia

Proyecto académico (CS2031 – DBP, UTEC, 2026-2). Uso educativo; no se incluye licencia de distribución.