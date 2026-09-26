# Checklist final de rúbrica — Proyecto quédate (DBP 2026-2, CS2031)

> Repositorio: [MiguelAngelDominguez/quedate-backend](https://github.com/MiguelAngelDominguez/quedate-backend) · Base: `develop` · Objetivo: 20/20 según rúbrica (ver `PLAN_ESTRUCTURAL.md` §9.2).
> Este documento verifica **cada ítem de la rúbrica** contra evidencia concreta del repositorio (PRs, commits, gate de verificación y archivos).

## Resumen de rúbrica

| Rúbrica | Puntos | Estado | Evidencia |
|---|---|---|---|
| §1 Entidades y Modelo | 3.0 | ✅ Cumplido | 12 entidades, JPA, constraints |
| §2 DTOs y Mapeo | 2.0 | ✅ Cumplido | >20 DTOs + ModelMapper |
| §3 Arquitectura y Patrones | 2.0 | ✅ Cumplido | Controller→Service→Repo, DI, SRP |
| §4 Manejo de Excepciones | 2.0 | ✅ Cumplido | 7+ excepciones + `@ControllerAdvice` |
| §5 Seguridad y Autenticación | 4.0 | ✅ Cumplido | JWT, roles, `@PreAuthorize`, BCrypt, CORS |
| §6 API REST y Controllers | 2.0 | ✅ Cumplido | Gates funcionales 24/24 y 24/24 |
| §7 Eventos y Asincronía | 2.0 | ✅ Cumplido | 3 eventos + email `@Async` |
| §8 Deployment | 2.0 | ⚠️ Documentado (fuera de alcance) | AWS fuera de alcance → bonus Docker Compose entregado |
| §9 GitHub y Documentación | 1.0 | ✅ Cumplido | GitFlow + PRs + Issues + README + postman |
| **TOTAL** | **20.0** | **19/20 verificado · 1 documentado** | |

---

## Sección por rúbrica

### §1 Entidades y Modelo (3.0) ✅
- **Requisito:** 12 entidades, relaciones JPA, unidireccionales cruzadas, constraints.
- **Evidencia:** `src/main/java/com/quedate/entity/` → `User`, `Role`, `Student`, `Landlord`, `University`, `Location`, `Room`, `Publication`, `RentalRequest`, `Visit`, `Review`, `Verification` (**12**). Relaciones `@ManyToOne`/`@OneToMany` unidireccionales, enums (`RoleName`, `RoomStatus`, `PublicationStatus`, `RentalRequestStatus`, `VisitStatus`, `VerificationStatus`, `VerificationType`), `@Column` con constraints, historial `@Version`/`createdAt`.
- **Issues/PRs:** #5, #7, #14, #23, #24 → PR #46–#67 (rama `feat/*`, merge a `develop`).

### §2 DTOs y Mapeo (2.0) ✅
- **Requisito:** >20 DTOs request/response + ModelMapper.
- **Evidencia:** `src/main/java/com/quedate/dto/` con paquetes `auth`, `user`, `university`, `location`, `room`, `publication`, `request`, `visit`, `review`, `verification`, `rating`, `security` (**>20 DTOs**, e.g. `RegisterRequestDTO`, `AuthResponseDTO`, `RoomCreateDTO`, `RoomUpdateDTO`, `RoomResponseDTO`, `PublicationResponseDTO`, `RentalRequestCreateDTO`/`ResponseDTO`, `VisitCreateDTO`/`ResponseDTO`, `ReviewCreateDTO`/`ResponseDTO`, `VerificationRequestDTO`/`DecisionDTO`/`ResponseDTO`, `RatingSummaryDTO`). Validaciones `@Valid`/`@NotNull`/`@Min`/`@Max`. `ModelMapper` en `Config` (PR #7).
- **Issues/PRs:** #7, #16, #25, #31 → PR #49, #52, #88, #53.

### §3 Arquitectura y Patrones (2.0) ✅
- **Requisito:** Controller→Service→Repo, controllers delgados, SRP, DI.
- **Evidencia:** capas `controller/` (solo orquestan), `service/` (negocio + transacciones `@Transactional`), `repository/` (Spring Data). Inyección por constructor. Rama base PR #3 (bootstrap capas) y #8/#9 (servicios base).
- **Issues/PRs:** #2, #3, #8, #9 → PR #47, #48.

### §4 Manejo de Excepciones (2.0) ✅
- **Requisito:** 7+ excepciones + `@ControllerAdvice`.
- **Evidencia:** `src/main/java/com/quedate/exception/` → `ResourceNotFoundException`, `DuplicateResourceException`, `InvalidOperationException`, `ForbiddenException`, `InvalidScheduleException`, `InvalidTransitionException`, `UnauthorizedException`, etc.; `GlobalExceptionHandler` (@RestControllerAdvice) centraliza 400/401/403/404/409/500. 
- **Issues/PRs:** #12, #22 → PR #59, #89 (404 en `getById`).

### §5 Seguridad y Autenticación (4.0) ✅
- **Requisito:** SecurityConfig+CORS, JWT (claims+expiración+refresh), roles + `@PreAuthorize`, BCrypt.
- **Evidencia:** `SecurityConfig` (stateless, `JwtAuthFilter`, `UserPrincipal`, `CORS`, rutas públicas `auth/**`, `GET /api/v1/landlords/**`, catálogo público) · `JwtService` (claims + expiración) · `@PreAuthorize("hasRole('ADMIN')")` / `hasRole('USER')` + checks de ownership por `user.id` · `PasswordEncoder BCrypt`. 
- **Verificación:** Gate M3 24/24 PASS incluye negativos 403 (rol/ownership). PRs #11, #90, #91.

### §6 API REST y Controllers (2.0) ✅
- **Requisito:** `/api/v1/...`, verbos correctos, códigos 200/201/204/400/401/403/404/409/500.
- **Evidencia:** 30+ endpoints bajo `/api/v1`. **Gates funcionales:** M2 **24/24 PASS** (`gate_m2.ps1`: CRUD 200/204/404, publish 201, my-rooms 200, negativos 403) · M3 **24/24 PASS** (`gate_m3.ps1`: rental 201/409, confirm 200, visits 201/200, review 201/409, verification 201/200, rating 200). 
- **Issues/PRs:** #18–#22, #26–#32 → PR #49–#67, #87–#90.

### §7 Eventos y Asincronía (2.0) ✅
- **Requisito:** `@EnableAsync` + eventos + `EmailService @Async` (3 casos).
- **Evidencia:** `UserRegisteredEvent` (🚀 registro), `RentalRequestCreatedEvent` (notifica landlord), `VisitScheduledEvent` (confirmación visita) + listeners en `event/`/`listener/`; `EmailService.sendHtml @Async` con `AsyncConfig`. 
- **Issues/PRs:** #13, #26 → PR #66, #90; validado en tests unitarios (`RentalRequestServiceTest.publishEvent`, #93).

### §8 Deployment (2.0) ⚠️ Documentado — fuera de alcance
- **Planteado:** AWS EC2 + RDS, env vars, security groups (`#34`).
- **Decisión del equipo:** despliegue cloud **fuera de alcance** (credenciales no disponibles); se entrega **Docker Compose** como bonus que cubre el despliegue local reproducible: `Dockerfile` multi-etapa (Maven → JRE), `docker-compose.yml` (postgres:16 + app, profile `prod`, puerto 8080, healthcheck), `application.yml` `ddl-auto: update` en prod. PR #92.
- **Nota de transparencia:** no se marca el punto hasta verificar accesibilidad real; ver issue #34.

### §9 GitHub y Documentación (1.0) ✅
- **Requisito:** GitFlow + PRs + Issues/Projects · README · postman_collection.json.
- **Evidencia:** `main` + `develop` (rama de trabajo); **30+ PRs** mergeados con base `develop` (incl. fixes #87–#91); cada issue vinculado a su PR y cerrado con evidencia; **36 de 39 issues cerrados** (3 = out-of-scope/pendiente documentado: #34); **milestones M0–M4 cerrados** (#7–#11); `README.md` (raíz, 11 secciones, PR #68); `postman_collection.json` (raíz, variables + auth heredada, PR #60). Informe de verificación: `docs/VERIFICACION_M0_M1.md`.

---

## Checklist operativo M0–M5

| Hito | Entregado | Estado |
|---|---|---|
| M0 Bootstrap (#1–#4) | estructura inicial + informe estructural | ✅ Issue #1–#4 · milestone #7 cerrado |
| M1 Seguridad (#5–#13) | auth, JWT, catálogo base | ✅ Iss. #5–#13 · milestone #8 cerrado · ver. M0/M1 |
| M2 Catálogo (#14–#22) | rooms + publicaciones | ✅ Iss. #14–#22 · milestone #9 cerrado · gate 24/24 |
| M3 Confianza (#23–#32) | rental/visitas/reviews/verificación | ✅ Iss. #23–#32 · milestone #10 cerrado · gate 24/24 |
| M4 Integración (#33–#35) | eventos+email (#33) · AWS (#34) · Docker (#35) | ⚠️ #33/#35 ✅ · #34 out-of-scope · milestone #11 cerrado |
| M5 Docs/QA (#36–#39) | postman/README/checklist/test | ✅ Iss. #36–#39 · milestone #12 cerrado |

## Bonus entregados
- ✅ **Docker Compose** (#35) → PR #92.
- ✅ **Tests unitarios > por dominio** (#39) → PR #93: 6 suites Mockito (Room, Publication, RentalRequest, Visit, Review, Verification), `mvn test` = **32 tests / 0 failures**.
- ✅ **Documentación rúbrica** (#38) → este archivo.

## Cómo reprobar la evidencia
1. `mvn clean test` → 32 tests verdes (PR #93).
2. Arrancar `mvn spring-boot:run` y correr los gates `%TEMP%\opencode\gate_m2.ps1` / `gate_m3.ps1` → 24/24 cada uno.
3. `docker compose up` (si hay Docker) → app + postgres listos (PR #92).
4. Revisar tablero de Issues/Projects y PRs cerrados con evidencia por hito.