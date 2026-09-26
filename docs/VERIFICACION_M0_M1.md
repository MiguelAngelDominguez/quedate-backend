# Verificación M0 Bootstrap + M1 Seguridad

Fecha: 25/09/2026 · Rama verificada: `develop`

Objetivo: demostrar con evidencia que los 13 issues de los hitos **M0 Bootstrap** y **M1 Seguridad** están completos e integrados en `develop`, y dejar los milestones cerrados.

## 1. Trazabilidad issue → rama → PR → merge

| Issue | Hito | Rama | PR | Merge |
|-------|------|------|-----|-------|
| #1 Bootstrap (repo, ramas, .gitignore, tablero) | M0 | `bootstrap` (commit directo) | — | `b530fa7` |
| #2 Proyecto Spring Boot Maven (JDK 26, Boot 4.1.1) | M0 | `spring-setup` (commit directo) | — | `b530fa7` |
| #3 application.yml + perfiles dev/prod + .env.example | M0 | `config-yaml` (commit directo) + fixes | #79 #80 #84 | `be862a6` `a2c968a` `69e9150` |
| #4 PLAN_ESTRUCTURAL.md | M0 | `plan-estructural` + docs | #40–#45 | `8e18e22`…`78753ad` |
| #5 Entidades base (User, Role, Student, Landlord, RoleName) | M1 | `feat/dominguez/base-entities` | #63 | `7d5d52f` |
| #6 Repos base | M1 | `feat/dominguez/base-repos` | #70 | `c969639` |
| #7 DTOs auth/usuarios + ModelMapper | M1 | `feat/dominguez/auth-dtos` | #71 | `ff8f7bf` |
| #8 AuthService (+ UserPrincipal, UserDetailsServiceImpl) | M1 | `feat/dominguez/auth-service` | #74 | `857d108` |
| #9 UserService, StudentService, LandlordService | M1 | `feat/dominguez/user-services` | #75 | `70ceb76` |
| #10 Controllers auth/usuarios | M1 | `feat/dominguez/auth-controllers` | #76 | `df9afd4` |
| #11 Seguridad JWT (JwtService, Filter, SecurityConfig, CORS) | M1 | `feat/dominguez/jwt-security` | #72 | `fb75159` |
| #12 Excepciones + GlobalExceptionHandler | M1 | `feat/dominguez/exceptions` | #69 | `f18acd8` |
| #13 Config transversal (Async, ModelMapper, DataSeeder, evento) | M1 | `feat/dominguez/cross-config` | #73 | `027f5b3` |

Nota: la protección de rama en `develop` se activó después de los commits iniciales de bootstrap; por eso #1–#3 usan commits directos y el resto usa PRs.

## 2. Verificación estructural

Artefactos presentes en `develop` (todos PASS):

- Bootstrap: `pom.xml`, `.gitignore`, `.env.example`, `src/main/resources/application.yml` (perfiles `dev` activo + documento `prod`), `PLAN_ESTRUCTURAL.md`, `FLUJO_DE_RECOMENDACION.md`, `QuedateApplication`.
- M1: `entity/{User,Role,Student,Landlord}.java` + `entity/enums/RoleName.java` · `repository/{User,Role,Student,Landlord}Repository.java` · `dto/auth/*` + `dto/user/*` + `dto/landlord/*` + `dto/student/*` + `dto/ErrorResponseDTO.java` · `security/{JwtService,JwtAuthenticationFilter,UserDetailsServiceImpl,UserPrincipal}.java` · `config/SecurityConfig.java` · `service/{auth,user}/*` · `controller/{auth,user,admin}/*` · `config/{AsyncConfig,ModelMapperConfig,DataSeeder}.java` · `event/UserRegisteredEvent.java` · `exception/*` (8 excepciones + handler).
- En `application.yml` el perfil `prod` (PostgreSQL vía env, SMTP autenticado, `ddl-auto: validate`) se agregó en la verificación (PR #84).

## 3. Gate de compilación

`mvn -q clean compile` sobre `develop` → **EXIT=0** (pom con postgresql + h2 runtime).

## 4. Gate de ejecución (perfil dev, H2)

App arranca en ~20 s; resultado de humos contra `http://localhost:8080`:

| # | Prueba | Esperado | Resultado |
|---|--------|----------|-----------|
| T1 | `GET /actuator/health` | 200 UP | **200 `{"groups":["liveness","readiness"],"status":"UP"}`** |
| T2 | `GET /api/v1/rooms` | 200 + rooms seed | **200, 2 rooms (Barranco/UTEC)** |
| T3 | `GET /api/v1/rooms/1` | 200 | **200 (detalle completo, images [])** |
| T4 | `POST /api/v1/auth/login` admin@quedate.com | 200 JWT+ADMIN | **200** |
| T5 | `POST /api/v1/auth/register` role USER | 201 | **201, roles=[USER]** |
| T6 | `GET /api/v1/users/me` con token | 200 identidad | **200 (admin y estudiante)** |
| T7 | `GET /api/v1/admin/users` con token LANDLORD | 403 | **403** |
| T8 | `GET /api/v1/users/me` sin token | 401 | **401** |
| T9 | `POST login` con clave incorrecta | 401 | **401** |
| T10 | Preflight CORS (origin localhost:3000) | 200 + header | **200, Allow-Origin=localhost:3000** |
| T11 | Preflight CORS (origin no permitido) | no permitido | **403** |

## 5. Hallazgos y correcciones durante la verificación

1. **`application.yml` sin perfil prod** → agregado documento `prod` (PR #84, `69e9150`).
2. **`AccessDeniedException` devolvía 500** (handler genérico) → ahora **403** (PR #85, `49a3af0`).
3. Nota: `register` acepta solo roles `USER`/`LANDLORD` (rechaza `STUDENT` como valor de `role` → 400).

## 6. Conclusión

- Compile gate verde y run gate con 11/11 humos PASS · issues #1–#13 cerrados · todo integrado en `develop`.
- Hitos **M0 Bootstrap (milestone #7)** y **M1 Seguridad (milestone #8)** verificados y cerrados.