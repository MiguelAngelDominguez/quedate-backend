# Verificación M2 Catálogo + M3 Confianza + M4/M5 Integración y Docs

Fecha: 25/09/2026 · Rama verificada: `develop` (tip `cc7dcb9`)

Objetivo: demostrar con evidencia que los hitos **M2 Catálogo**, **M3 Confianza**, **M4 Integración** y **M5 Docs/QA** están completos e integrados en `develop`, mediante pruebas funcionales (gates), correcciones de bugs, entregables bonus (Docker, tests) y el cierre de issues y milestones.

## 1. Trazabilidad issue → rama → PR → merge (M2 y M3)

| Issue | Hito | Rama | PR | Merge |
|-------|------|------|-----|-------|
| #14 University | M2 | `feat/andremejia/university` | #46 | `6642de7` |
| #15 Location | M2 | `feat/andremejia/location` | #47 | `865880a` |
| #16 RoomDTO | M2 | `feat/andremejia/room-dto` | #49 | `b404fc9` |
| #17 RoomService | M2 | `feat/andremejia/room-service` | #50 | `17fbe0e` |
| #18 RoomController | M2 | `feat/andremejia/room-controller` | #49 · #51 | `b404fc9` · `e84e5dd` |
| #19 Búsqueda (universidad/ubicación/precio/distancias) | M2 | `feat/andremejia/room-search` | #49 · #54 | `b404fc9` · `37ab2d7` |
| #20 Publicación | M2 | `feat/andremejia/publish` | #56 · #87 | `d829a87` · `e17f1b5` |
| #21 CRUD/mis-rooms | M2 | `feat/andremejia/my-rooms` | #57 · #87 | `ceea410` · `e17f1b5` |
| #22 Eliminación segura room→publicación | M2 | `feat/andremejia/room-delete` | #59 · #87 · #88 | `ec63515` · `e17f1b5` · `5716858` |
| #23 RentalRequest | M3 | `feat/iygt8/request` | #48 | `bf42a45` |
| #24 Visit | M3 | `feat/iygt8/visit` | #52 | `5c80459` |
| #25 Review | M3 | `feat/iygt8/review` | #53 | `1e408d1` |
| #26 Verification | M3 | `feat/iygt8/verification` | #55 · #78 · #90 | `3214098` · `ab57b0f` · `b62f50c` |
| #27 Rating | M3 | `feat/iygt8/rating` | #58 · #90 | `4688cea` · `b62f50c` |
| #28 Controllers confianza | M3 | `feat/iygt8/trust-controllers` | #61 · #90 | `5532515` · `b62f50c` |
| #29 Email/notificación | M3 | `feat/iygt8/email` | #62 · #90 | `4234c28` · `b62f50c` |
| #30 Perfil landlord | M3 | `feat/iygt8/landlord-profile` | #65 | `8ac710c` |
| #31 DTOs confianza | M3 | `feat/iygt8/trust-dtos` | #66 | `a37c947` |
| #32 QA confianza | M3 | `feat/iygt8/trust-qa` | #67 | `37d667b` |

## 2. Bugs encontrados y corregidos durante la verificación

La verificación funcional (no solo estructural) destapó huecos reales que los gates fallaron y se corrigieron:

| Bug | Fix | PR (merge) |
|---|---|---|
| RoomService sin CRUD completo (solo search/getByLandlord) | CRUD (create/update/delete) + `getByCurrentLandlord` con guardas | #87 `e17f1b5` |
| `my-rooms` devolvía `Page.empty()` | controller real + servicio `getByCurrentLandlord` | #87 `e17f1b5` |
| Controllers de publicación sin verificación de rol/ownership | `@PreAuthorize` + checks de propiedad en servicio | #87 `e17f1b5` |
| Publicación devolvía entidad (acoplamiento) | `PublicationResponseDTO` + `@Transactional` | #88 `5716858` |
| FK: borrar room fallaba si tenía publicación | `deleteByRoom_Id` antes de borrar el room | #88 `5716858` |
| `getById(room)` devolvía 500/null | `ResourceNotFoundException` → 404 | #89 `a4adacb` |
| M3 roto por rol inexistente `ROLE_STUDENT` y principal no resoluble | `UserPrincipal` + rol `USER` + ownership por `user.id` en request/visit/review/verification | #90 `b62f50c` |
| GET rating /landlords/{id}/rating daba 401 (no auth pública) | `permitAll` en `SecurityConfig` para `GET /api/v1/landlords/**` | #91 `e47c733` |

## 3. Verificación funcional (gates)

Se registraron usuarios de prueba (`ana.m2@quedate.com` role USER, `luis.m2@quedate.com` role LANDLORD) y se ejecutaron dos gates con entidad real HTTP (+ token JWT) sobre la app arrancada en `develop`.

### Gate M2 — Catálogo: **24/24 PASS**
- Universidades/locaciones/rooms: GET 200 público · POST/PUT/DELETE solo admin.
- CRUD rooms: create 201 · get 200/404 · update 200 · delete 204/404 · negativos 403 (rol/ownership).
- Publicación: publish 201 · archive 200 · repetir 409 · publicar sobre room no disponible 409.
- Búsqueda por universidad/ubicación · `my-rooms` 200 · negativos 403 para no propietarios.

### Gate M3 — Confianza: **24/24 PASS**
- Rental request: create 201 · duplicado 409 · status PENDING→CONFIRMED 200 (solo landlord owner) · transición inválida 400 · 403 a ajenos.
- Visitas: schedule 201 (partes involucradas) · 403 a no involucrados · PENDING→CONFIRMED 200 · student CONFIRMED→COMPLETED 200 · 403 si lo hace el landlord.
- Review: create 201 (con rental CONFIRMED o visita COMPLETADA) · duplicado 409 · 403 sin experiencia previa.
- Verificación: request 201 · decision APPROVED/REJECTED 200 (admin) · 403 no-admin.
- Rating: GET `/api/v1/landlords/{id}/rating` 200 (público) · valor correcto tras reviews.

## 4. M4 Integración (issues #33–#35)

| Issue | Estado | Evidencia |
|---|---|---|
| #33 Integración (eventos → email) | ✅ Cerrado | eventos `UserRegistered`, `RentalRequestCreated`, `VisitScheduled` + email `@Async` (lista en `event/listener` + `EmailService`) |
| #34 Deployment AWS | ⚠️ Fuera de alcance | Sin credenciales cloud; se documenta en `docs/CHECKLIST_RUBRICA.md` §8. Se entregó **Docker Compose** como alternativa reproducible |
| #35 Docker (bonus) | ✅ Cerrado | PR #92 (`c61c8e0`): `Dockerfile` multi-etapa, `docker-compose.yml` (postgres:16 + app prod puerto 8080, healthcheck), prod `ddl-auto: update` |

> Nota: Docker no se pudo ejecutar localmente (sin Docker en el entorno). El PR documenta que los tags de imagen (`maven:3.9.11-eclipse-temurin-26`, `eclipse-temurin:26-jre`) deben existir en Docker Hub; si no, se usan tags equivalentes.

## 5. M5 Docs/QA (issues #36–#39)

| Issue | Estado | Evidencia |
|---|---|---|
| #36 postman_collection.json | ✅ Cerrado | PR #60 `0ea7519` (raíz, variables + auth heredada) |
| #37 README.md | ✅ Cerrado | PR #68 `8ba5679` (raíz, 11 secciones) |
| #38 Checklist rúbrica 20/20 | ✅ Cerrado | PR #94 (`cc7dcb9`): `docs/CHECKLIST_RUBRICA.md` |
| #39 Tests unitarios (bonus) | ✅ Cerrado | PR #93 (`6f07d16`): 6 suites Mockito → `mvn test` **32/32 PASS** |

## 6. Build del entregable final

```bash
mvn clean compile  # PASS (JDK 26, Spring Boot 4.1.1)
mvn test           # 32 tests, 0 failures, 0 errors (incl. QuedateApplicationTests)
```

## 7. Estado de issues y milestones

- **36/39 issues cerrados** en el repositorio; pendientes documentados: **#34** (AWS out-of-scope, con nota de transparencia).
- Milestones cerrados: #7 (M0), #8 (M1), #9 (M2), #10 (M3), #11 (M4), #12 (M5).
- Reviewer de PRs de esta fase: `dominguez` (A) como admin en nombre del equipo.

## 8. Riesgos y transparencia

- **§8 Deployment:** puntaje no reclamado hasta validar; se documenta explícitamente la no implementación AWS.
- **Docker:** verificado a nivel de configuración, no ejecutado en este entorno.
- Los gates usan usuarios registrados en H2 en memoria (por defecto `dev`); con BD real deben re-registrarse una vez.