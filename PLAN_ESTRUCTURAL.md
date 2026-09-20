# PLAN ESTRUCTURAL — Proyecto quédate (DBP 2026-2)

**quédate** — Plataforma de habitaciones verificadas para becarios UTEC (campus Miraflores).
"¿Dónde queda tu U? quédate cerca."

> **Curso:** CS2031 — Diseño y Bases de Datos (DBP) · Ciclo 2026-2 · UTEC
> **Deadline:** viernes 25 de septiembre de 2026, 23:59
> **Puntaje objetivo:** 20/20 según rúbrica
> **Entorno de desarrollo:** IntelliJ IDEA (Ultimate, licencia educativa) · JDK 26 · Spring Boot 4.1.x · Maven

---

## Integrantes

| Rol | GitHub | Área |
|---|---|---|
| **A** (fundación + seguridad) | @MiguelAngelDominguez | Auth JWT, usuarios, roles, excepciones, config base |
| **B** (catálogo + búsqueda) | @andremejia-hub | Habitaciones, universidades, ubicaciones, publicaciones, distancias |
| **C** (confianza + comunicación) | @iygt8-iterate | Solicitudes, visitas, reseñas, verificación, eventos + correo |

> Los números de issues hacen referencia a la numeración real del repositorio GitHub. Cada sección indica su(s) rama(s) `feat/<alias>/<tarea>`; el mapa completo issue→sección→rama está en el Apéndice D.

---

## Persona A — Todo lo que le toca `[issues → #1, #2, #3, #4, #5, #6, #7, #8, #9, #10, #11, #12, #13]`

### A0. Bootstrap del repo + informe estructural `[issues → #1, #4]` · ramas: `feat/dominguez/bootstrap`, `feat/dominguez/plan-estructural`

- **#1 — Bootstrap** · rama `feat/dominguez/bootstrap`: repo público, ramas `main` + `develop` protegidas (1 review obligatorio), colaboradores, `.gitignore`, labels y milestones, tablero GitHub Projects. ✅ hecho en M0.
- **#4 — Informe estructural** · rama `feat/dominguez/plan-estructural`: este documento `PLAN_ESTRUCTURAL.md` (reparto A/B/C, rutas 📍 y mapeo issue→sección→rama del Apéndice D). ✅ hecho en M0.

### A1. Endpoints que implementa `[issues → #10]` · rama: `feat/dominguez/auth-controllers`

| Método | Ruta | Rol | Request (body/params) | Response | Códigos |
|---|---|---|---|---|---|
| POST | `/api/v1/auth/register` | Público | `RegisterRequestDTO` (firstName, lastName, email, password, phone, role: STUDENT/LANDLORD) | `AuthResponseDTO` | 201, 400 (password débil/campos inválidos), 409 (email duplicado) |
| POST | `/api/v1/auth/login` | Público | `LoginRequestDTO` (email, password) | `AuthResponseDTO` | 200, 401 (credenciales malas) |
| POST | `/api/v1/auth/refresh` | Público | `RefreshTokenRequestDTO` | `AuthResponseDTO` (access renovado) | 200, 401 (refresh inválido/expirado) |
| GET | `/api/v1/users/me` | Cualquier autenticado | — | `UserResponseDTO` | 200, 401 |
| PUT | `/api/v1/users/me` | Autenticado | `UserUpdateDTO` | `UserResponseDTO` | 200, 400, 401 |
| GET | `/api/v1/students/{id}` | Autenticado | path `id` | `StudentResponseDTO` | 200, 404 |
| PUT | `/api/v1/landlords/me/profile` | `LANDLORD` | `LandlordUpdateDTO` | `LandlordResponseDTO` | 200, 400, 403 |
| GET | `/api/v1/admin/users` | `ADMIN` | opc. paginación | `Page<UserResponseDTO>` | 200, 403 |

**Dónde se implementan:**
- `/api/v1/auth/*` → `controller/auth/AuthController.java`
- `/api/v1/users/me` → `controller/user/UserController.java`
- `/api/v1/students/{id}` → `controller/user/StudentController.java`
- `/api/v1/landlords/me/profile` → `controller/user/LandlordController.java`
- `/api/v1/admin/users` → `controller/admin/AdminController.java`
- Lógica en `service/auth/AuthService.java`, `service/user/UserService.java`, `service/user/StudentService.java`, `service/user/LandlordService.java`.

### A2. Clase principal y configuración base `[issues → #2, #3]` · ramas: `feat/dominguez/spring-setup`, `feat/dominguez/config-yaml`

**`QuedateApplication.java`** — 📍 `src/main/java/com/quedate/QuedateApplication.java`
- *Implementar:* clase con `@SpringBootApplication` y método `main`.
- *Qué hace:* inicia Spring Boot (es el punto de entrada).
- *Retorno:* nada (arranca la app). Verificación: `mvn compile` y `mvn spring-boot:run` funcionan.

**`pom.xml`** — 📍 raíz del proyecto
- *Implementar:* Spring Boot `4.1.1` (parent), Java 26; dependencias Web, Data JPA, Security, Validation, Mail, Thymeleaf, Actuator, H2 (dev), PostgreSQL (prod), Lombok, ModelMapper, jjwt (api/impl/jackson), starter-test, security-test.
- *Qué hace:* define build y ejecución.
- *Retorno:* artefacto compilable. **Regla: solo A lo modifica; B/C piden dependencias por issue.**

**`application.yml`** + perfiles — 📍 `src/main/resources/application.yml` · `application-dev.yml` (H2 en memoria) · `application-prod.yml` (PostgreSQL vía variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `REFRESH_EXPIRATION`, `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `CORS_ALLOWED_ORIGINS`).
- *Implementar:* perfiles `dev` (H2 en memoria) y `prod` (PostgreSQL + env vars); `ddl-auto` según entorno.
- *Qué hace:* configurar la app según el entorno.
- *Retorno:* configuración cargada al arrancar.

**`.gitignore`** — 📍 raíz · excluye `target/`, `.idea/`, `*.iml`, `.env`, `application-prod.yml`, logs. **`.env.example`** — 📍 raíz · plantilla de variables sin valores reales.

### A3. Entidades (tablas) que crea `[issues → #5]` (+ `DataSeeder` en #13) · rama: `feat/dominguez/base-entities` (+ `feat/dominguez/cross-config` para el seeder)

**`User`** — 📍 `src/main/java/com/quedate/entity/User.java`
- *Implementar:* `Long id`, `String firstName`, `String lastName`, `String email` (`@Column(nullable=false, unique=true)` + `@NotBlank` + `@Email`), `String password` (hash BCrypt), `String phone`, `@ManyToMany(fetch=EAGER) Set<Role> roles`, `LocalDateTime createdAt`, `LocalDateTime updatedAt`.
- *Qué hace:* usuario base de todos los roles; fuente de autenticación.
- *Retorno:* getters/setters; **el password nunca se serializa en DTOs**.

**`Role`** — 📍 `entity/Role.java`
- *Implementar:* `Long id`, `@Enumerated(EnumType.STRING) RoleName name` donde `RoleName = {USER, LANDLORD, ADMIN, MANAGER}`.
- *Qué hace:* catálogo de roles para authorities/JWT.
- *Retorno:* nombre del rol (authority).

**`Student`** — 📍 `entity/Student.java`
- *Implementar:* `Long id`, `@OneToOne User user`, `String dni` (unique, `@Pattern` 8 dígitos), `String program`, `Integer entryYear`, `String scholarshipCode` (opcional).
- *Qué hace:* perfil del estudiante.
- *Retorno:* perfil estudiante.

**`Landlord`** — 📍 `entity/Landlord.java`
- *Implementar:* `Long id`, `@OneToOne User user`, `dni` (unique), `bio`, `boolean isVerified = false`, `createdAt`.
- *Qué hace:* perfil del landlord; `isVerified` lo actualiza el servicio de Verificación de C (badge).
- *Retorno:* perfil landlord.

**`DataSeeder`** (`CommandLineRunner`) — 📍 `config/DataSeeder.java`
- *Implementar:* precarga los 4 roles, un ADMIN demo (`admin@quedate.com`), la universidad **UTEC – Miraflores** (coordinar con B: si A se adelanta lo crea A; si B ya lo tiene, no duplicar) y 2-3 rooms demo.
- *Qué hace:* poblar datos de demostración para el docente.
- *Retorno:* registros en BD.

### A4. Repositories que crea `[issues → #6]` · rama: `feat/dominguez/base-repos`

📍 `src/main/java/com/quedate/repository/UserRepository.java`, `RoleRepository.java`, `StudentRepository.java`, `LandlordRepository.java`.

| Clase | Métodos | Qué hace | Retorno |
|---|---|---|---|
| `UserRepository` | `findByEmail(String)` | busca por email (login) | `Optional<User>` |
| `UserRepository` | `existsByEmail(String)` | valida unicidad al registrar | `boolean` |
| `RoleRepository` | `findByName(RoleName)` | carga el rol | `Optional<Role>` |
| `StudentRepository` | `findByUserId(Long)` | perfil por usuario | `Optional<Student>` |
| `LandlordRepository` | `findByUserId(Long)`, `existsByDni(String)` | perfil landlord y DNI único | `Optional` / `boolean` |

### A5. Servicios que crea `[issues → #8, #9]` · ramas: `feat/dominguez/auth-service`, `feat/dominguez/user-services`

**`AuthService`** — 📍 `service/auth/AuthService.java`
- `register(RegisterRequestDTO dto)`
  - *Implementar:* validar `@Valid` del DTO; `userRepository.existsByEmail(dto.getEmail())`; aplicar regla de password (mín 8, mayúscula, número); `passwordEncoder.encode(...)`; crear `User` + asignar `Role` (USER o LANDLORD según el request) + crear su `Student`/`Landlord`; `userRepository.save(user)`; `applicationEventPublisher.publishEvent(new UserRegisteredEvent(user))`.
  - *Qué hace:* ① email existente → `EmailAlreadyExistsException` (409); ② password débil → `InvalidOperationException` (400); ③ codifica con **BCrypt**; ④ crea usuario + rol + perfil; ⑤ guarda; ⑥ **publica `UserRegisteredEvent`** (welcome email lo envía C).
  - *Retorno:* `AuthResponseDTO` (accessToken, refreshToken, userInfo) · endpoint 201.
- `login(LoginRequestDTO dto)`
  - *Implementar:* `authenticationManager.authenticate(...)` contra `UserDetailsServiceImpl`; `jwtService.generateToken(principal)` (claims userId, email, roles) y `jwtService.generateRefreshToken(principal)`.
  - *Qué hace:* valida credenciales; si fallan → `BadCredentialsException` (401); emite access + refresh token.
  - *Retorno:* `AuthResponseDTO` · endpoint 200.
- `refresh(RefreshTokenRequestDTO dto)`
  - *Implementar:* `jwtService.isValid(refresh)` + `isExpired(refresh)`; si ok, generar nuevo access token.
  - *Qué hace:* renueva el access sin pedir login; inválido → `InvalidTokenException` (401).
  - *Retorno:* `AuthResponseDTO` con access renovado · endpoint 200.
- `getCurrentUser()`
  - *Implementar:* leer `SecurityContext` → `UserPrincipal` → cargar perfil por `getUserId()`.
  - *Qué hace:* devuelve el propio usuario autenticado.
  - *Retorno:* `UserResponseDTO` (sin password) · endpoint GET /users/me.

**`UserService`** — 📍 `service/user/UserService.java` · `getById(id)` → `UserResponseDTO` (404 si no existe, `ResourceNotFoundException`); `update(id, dto)` → `UserResponseDTO`; `lista admin` con paginación → `Page<UserResponseDTO>`.
**`StudentService`** — 📍 `service/user/StudentService.java` · `getProfileByUserId(id)` → `StudentResponseDTO`; `update(...)` → `StudentResponseDTO`.
**`LandlordService`** — 📍 `service/user/LandlordService.java` · `getProfileByUserId(...)`, `updateProfile(actor, dto)` (solo el dueño, si no → `ForbiddenException` 403), `getById` → `LandlordResponseDTO`.

### A6. Seguridad (todo el motor) `[issues → #11]` · rama: `feat/dominguez/jwt-security`

📍 `config/SecurityConfig.java` · `security/JwtService.java` · `security/JwtAuthenticationFilter.java` · `security/UserDetailsServiceImpl.java` · `security/UserPrincipal.java`.

**`SecurityConfig`** — 📍 `config/SecurityConfig.java`
- *Implementar:* bean `SecurityFilterChain`: `.csrf(disable)`, sesiones `STATELESS`, `.httpBasic(disable)`, `.exceptionHandling` con entry point JSON (401), `.cors()` con `CorsConfigurationSource`, `.authorizeHttpRequests` (públicas: `"/api/v1/auth/**"`, `GET /api/v1/rooms/**`, `GET /api/v1/universities/**`, `GET /api/v1/locations/**`, Swagger; resto `.anyRequest().authenticated()`), y `.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)`. `@EnableMethodSecurity` para `@PreAuthorize`.
- *Qué hace:* define qué se protege y cómo.
- *Retorno:* bean `SecurityFilterChain`.

**`JwtService`** — 📍 `security/JwtService.java`
- *Implementar:* `generateToken(UserPrincipal)` → `String`; `generateRefreshToken(...)` → `String`; `extractUserId/Email/Roles(token)` → claims; `isValid(token)` → `boolean`; `isExpired(token)` → `boolean`. Firman con `JWT_SECRET` (de `.env`).
- *Qué hace:* firma y valida tokens.
- *Retorno:* tokens `String` o claims, según método.

**`JwtAuthenticationFilter`** (`OncePerRequestFilter`) — 📍 `security/JwtAuthenticationFilter.java`
- *Implementar:* en `doFilterInternal`, extraer `Authorization: Bearer token`, validar con `JwtService`; si ok, cargar `UserDetails` vía `UserDetailsServiceImpl` y setear `SecurityContext`; si no, sigue sin autenticar (rutas protegidas → 401).
- *Qué hace:* autentica cada request con el token.
- *Retorno:* void (efecto: contexto de seguridad).

**`UserDetailsServiceImpl`** — 📍 `security/UserDetailsServiceImpl.java` · `loadUserByUsername(email)` → `UserPrincipal` o `UsernameNotFoundException` (→ 401). Retorno: `UserDetails`.
**`UserPrincipal`** — 📍 `security/UserPrincipal.java` · envuelve `User`: `getUserId()`, `getEmail()`, `getAuthorities()` (roles), password hash. Retorno: usuario autenticado en controllers/servicios.

### A7. Excepciones y respuesta de error `[issues → #12]` · rama: `feat/dominguez/exceptions`

📍 `src/main/java/com/quedate/exception/` · `GlobalExceptionHandler.java` · `dto/ErrorResponseDTO.java`.

- **Excepciones personalizadas (A crea 7):** `ResourceNotFoundException` (404), `DuplicateResourceException` (409), `EmailAlreadyExistsException` (409, extiende la anterior), `InvalidOperationException` (400), `UnauthorizedException` (401), `ForbiddenException` (403), `InvalidTokenException` (401). (B y C agregan `RoomNotAvailableException.java` e `InvalidScheduleException.java`.)
- *Implementar:* cada excepción en su `.java` con `@ResponseStatus` o handler; status code según la lista.
- *Qué hace:* modelar errores de negocio con su HTTP code.
- *Retorno:* excepciones lanzadas por B/C también.

**`GlobalExceptionHandler`** (`@ControllerAdvice`) — 📍 `exception/GlobalExceptionHandler.java`
- *Implementar:* `@ExceptionHandler` para cada personalizada + `MethodArgumentNotValidException` + `HttpMessageNotReadableException` + fallback 500.
- *Qué hace:* captura todas y las normaliza al formato único.
- *Retorno:* `ResponseEntity<ErrorResponseDTO>` con `timestamp, status, error, message, path`.

**`ErrorResponseDTO`** — 📍 ruta oficial `dto/ErrorResponseDTO.java` · campos `timestamp, status, error, message, path`; formato único para toda la API.

### A8. Config transversal `[issues → #13]` · rama: `feat/dominguez/cross-config`

- **`AsyncConfig`** — 📍 `config/AsyncConfig.java` · `@EnableAsync` + bean `ThreadPoolTaskExecutor` (core 4, max 8). *Implementar/Qué hace:* procesa los emails de C en hilos secundarios (la API responde al instante). *Retorno:* `Executor`.
- **`ModelMapperConfig`** — 📍 `config/ModelMapperConfig.java` · bean `ModelMapper` (strict, sin mapear passwords). *Qué hace:* mapeo entidad↔DTO sin fugas. *Retorno:* mapper compartido.
- **`UserRegisteredEvent`** — 📍 `event/UserRegisteredEvent.java` · payload `User`. *Qué hace:* lo publica `AuthService.register` y lo escucha el listener de C (welcome email). *Retorno:* evento (lo consume C).

### A9. DTOs de auth/usuarios + ModelMapper `[issues → #7]` · rama: `feat/dominguez/auth-dtos`

📍 `dto/auth/` (`RegisterRequestDTO`, `LoginRequestDTO`, `RefreshTokenRequestDTO`, `AuthResponseDTO`) · `dto/user/` (`UserResponseDTO`, `UserUpdateDTO`) · `dto/student/StudentResponseDTO` · `dto/landlord/` (`LandlordResponseDTO`, `LandlordUpdateDTO`).

- *Implementar:* `@Valid` en cada request (`@NotBlank`, `@Email`, `@Size`, `@Pattern`...); **jamás** se serializa el password en responses; mapeo con ModelMapper (bean estricto de `ModelMapperConfig`, #13) o mapeo manual en los DTOs; sin fugas de entidades a la API.
- *Qué hace:* contratos request/response de auth y usuarios; separación de capas.
- *Retorno:* DTOs con validaciones y sin datos sensibles.

---

## Persona B — Todo lo que le toca `[issues → #14, #15, #16, #17, #18, #19, #20, #21, #22]`

### B1. Endpoints que implementa `[issues → #18, #19, #21, #22]` · ramas: `feat/andremejia/university`, `feat/andremejia/location`, `feat/andremejia/room-controllers`, `feat/andremejia/publication`

| Método | Ruta | Rol | Request | Response | Códigos |
|---|---|---|---|---|---|
| GET | `/api/v1/universities` | Público | — | `List<UniversityDTO>` | 200 |
| GET | `/api/v1/universities/{id}` | Público | path id | `UniversityDTO` | 200, 404 |
| POST | `/api/v1/universities` | `ADMIN` | `UniversityCreateDTO` | `UniversityDTO` | 201, 400, 403 |
| GET | `/api/v1/locations` | Público | ?district= | `List<LocationDTO>` | 200 |
| GET | `/api/v1/rooms` | Público | query `minPrice, maxPrice, district, universityId, page, size, sortBy=price/distance` | `Page<RoomSummaryDTO>` | 200 |
| GET | `/api/v1/rooms/{id}` | Público | path id | `RoomDetailDTO` | 200, 404 |
| POST | `/api/v1/rooms` | `LANDLORD` | `RoomCreateDTO` | `RoomDetailDTO` | 201, 400, 403 |
| PUT | `/api/v1/rooms/{id}` | `LANDLORD` dueño o `ADMIN` | `RoomUpdateDTO` | `RoomDetailDTO` | 200, 403, 404 |
| DELETE | `/api/v1/rooms/{id}` | dueño o `ADMIN` | path id | vacío | 204, 403, 404 |
| GET | `/api/v1/my-rooms` | `LANDLORD` | — | `List<RoomSummaryDTO>` | 200, 403 |
| GET | `/api/v1/landlords/{id}/rooms` | Público | path id | `List<RoomSummaryDTO>` | 200, 404 |
| POST | `/api/v1/my-rooms/{id}/publish` | `LANDLORD` dueño | — | `PublicationDTO` | 201, 403, 404 |
| POST | `/api/v1/my-rooms/{id}/archive` | dueño | — | `PublicationDTO` | 200, 403 |

**Dónde se implementan:** `/api/v1/rooms*` y `/api/v1/my-rooms*` → `controller/room/RoomController.java` (lógica en `service/room/RoomService.java`) · `/api/v1/universities*` → `controller/university/UniversityController.java` · `/api/v1/locations*` → `controller/location/LocationController.java` · publicar/archivar → `controller/room/PublicationController.java`.

### B2. Entidades que crea `[issues → #14]` · rama: `feat/andremejia/catalog-entities`

**`University`** — 📍 `entity/University.java`
- *Implementar:* `Long id`, `String name` (`@NotNull`), `String campusName`, `String address`, `Double latitude`, `Double longitude`, `String website`.
- *Qué hace:* ancla geográfica "cerca de tu U".
- *Retorno:* datos de la universidad. Dato esperado en el Seeder (con A): UTEC, Campus Miraflores, coords reales.

**`Location`** — 📍 `entity/Location.java`
- *Implementar:* `Long id`, `String district` (`@NotNull`), `String address`, `Double latitude`, `Double longitude`, `Double distanceToUniversityKm` (se calcula).
- *Qué hace:* ubicación física de la habitación.
- *Retorno:* datos de ubicación.

**`Room`** — 📍 `entity/Room.java`
- *Implementar:* `Long id`, `String title` (`@NotBlank`, `@Size(max=100)`), `String description`, `BigDecimal price` (`@NotNull`, `@Min(1)`), `Integer capacity` (`@Min(1)`), `Double sizeM2`, `@ElementCollection List<String> images`, `RoomStatus status` (`AVAILABLE`/`RENTED`), `boolean isVerified = false`, `@ManyToOne Landlord owner`, `@ManyToOne University university`, `@ManyToOne Location location`, `createdAt`, `updatedAt`. Dueño = LANDLORD; `isVerified` lo actualiza C.
- *Qué hace:* el producto central; dueño = LANDLORD; `isVerified` lo actualiza C al aprobar.
- *Retorno:* datos del alojamiento.

**`Publication`** — 📍 `entity/Publication.java`
- *Implementar:* `Long id`, `@ManyToOne Room room`, `PublicationStatus status` (`ACTIVE`/`ARCHIVED`), `publishedAt`, `archivedAt`.
- *Qué hace:* gestiona publicar/archivar ("gestión de publicaciones desde el perfil").
- *Retorno:* registro de publicación.

### B3. Repositories que crea `[issues → #15]` · rama: `feat/andremejia/catalog-repos`

📍 `repository/RoomRepository.java`, `UniversityRepository.java`, `LocationRepository.java`, `PublicationRepository.java`.

| Clase | Método clave | Qué hace | Retorno |
|---|---|---|---|
| `RoomRepository` | `findByOwner_Id(Long)` | rooms de un landlord | `List<Room>` |
| `RoomRepository` | `search(...)` `@Query`/Specification (`universityId`, `district`, `minPrice`, `maxPrice`, `status=AVAILABLE`) + `Pageable` | búsqueda filtrada | `Page<Room>` |
| `UniversityRepository` | `findByNameContainingIgnoreCase`, `findAll` | búsqueda/lista | `List` |
| `LocationRepository` | `findByDistrictIgnoreCase(String)` | filtrar por distrito | `List<Location>` |
| `PublicationRepository` | `findByRoom_Id`, `findByStatus` | publicaciones | `List<Publication>` |

### B4. Servicios que crea `[issues → #16, #20]` · ramas: `feat/andremejia/distance-calculator`, `feat/andremejia/room-service`

**`RoomService`** — 📍 `service/room/RoomService.java`
- `create(actor, RoomCreateDTO)` — *Implementar:* validar `@Valid` (price>0, capacity≥1); crear `Room` con el `LANDLORD` autenticado. *Qué hace:* da de alta la habitación del arrendador. *Retorno:* `RoomDetailDTO` (201).
- `update(actor, id, dto)` — *Implementar:* comprobar dueño/ADMIN; si no es dueño ni ADMIN → `ForbiddenException` (403). *Qué hace:* actualiza datos del room. *Retorno:* `RoomDetailDTO` (200).
- `delete(actor, id)` — *Implementar:* misma validación que update. *Qué hace:* borra o archiva. *Retorno:* void (204).
- `getById(id)` — *Implementar:* `roomRepository.findById`; si no → `ResourceNotFoundException` (404). *Retorno:* `RoomDetailDTO`.
- `search(RoomSearchFilterDTO, Pageable)` — *Implementar:* `roomRepository.search(...)` con filtros + `Pageable`; orden opcional por cercanía (Haversine) y precio. *Qué hace:* búsqueda filtrada/paginada. *Retorno:* `Page<RoomSummaryDTO>`.
- `getByLandlord(id)` / `getMyRooms(actor)` — *Implementar:* `roomRepository.findByOwner_Id(...)`. *Retorno:* `List<RoomSummaryDTO>`.
- `publish(actor, roomId)` / `archive(...)` — *Implementar:* crear `Publication` (ACTIVE/ARCHIVED) con fechas. *Qué hace:* gestiona la publicación desde el perfil. *Retorno:* `PublicationDTO`.

**`UniversityService`** — 📍 `service/university/UniversityService.java` · `list()` → `List<UniversityDTO>`; `getById` (404); `create(ADMIN)` → (201).
**`LocationService`** — 📍 `service/location/LocationService.java` · `list(district?)` → `List<LocationDTO>`; `create(ADMIN)`.
**`DistanceCalculator`** — 📍 `util/DistanceCalculator.java` · `haversine(lat1, lng1, lat2, lng2)` → `double` km (cercanía al campus sin pagar Google Maps).

### B5. DTOs que crea `[issues → #17]` · rama: `feat/andremejia/catalog-dtos`

📍 `dto/room/` (`RoomCreateDTO`, `RoomUpdateDTO`, `RoomDetailDTO`, `RoomSummaryDTO`, `RoomSearchFilterDTO`) · `dto/university/` (`UniversityDTO`, `UniversityCreateDTO`) · `dto/location/LocationDTO` · `dto/publication/PublicationDTO`.

- *Implementar:* `RoomCreateDTO` (title, description, price, capacity, sizeM2, images[], locationId, universityId) · `RoomUpdateDTO` (mismos, opcionales + status) · `RoomDetailDTO` (todo + id, ownerName, district, universityName, averageRating (lo llena C), isVerified, distanceKm) · `RoomSummaryDTO` (id, title, price, primera imagen, district, universityName, distanceKm) · `RoomSearchFilterDTO` (minPrice, maxPrice, district, universityId, page, size, sortBy) · `UniversityDTO`/`UniversityCreateDTO` · `LocationDTO` · `PublicationDTO`.
- *Qué hace:* contratos request/response del catálogo; sin exponer entidades.
- *Retorno:* DTOs con validaciones (`@Valid` en request: `@NotNull`, `@Min`…).

### B6. Regla de seguridad que aplica `[issues → #18, #19, #21, #22]` · ramas: (mismas de B1)

`@PreAuthorize("hasRole('LANDLORD')")` y `hasRole('ADMIN')` **en cada método del controller** (`RoomController.java`, `UniversityController.java`); verificación de "es el dueño" (con `getCurrentUser()`) **dentro del service**. No se toca el `SecurityConfig` de A. *Retorno:* 403/404 según el caso; el motor (roles+JWT) es de A.

---

## Persona C — Todo lo que le toca `[issues → #23, #24, #25, #26, #27, #28, #29, #30, #31, #32]`

### C1. Endpoints que implementa `[issues → #26, #27, #28, #29]` · ramas: `feat/iygt8/rental-request`, `feat/iygt8/visit`, `feat/iygt8/review`, `feat/iygt8/verification`

| Método | Ruta | Rol | Request | Response | Códigos |
|---|---|---|---|---|---|
| GET | `/api/v1/my-rental-requests` | `STUDENT` | — | `List<RentalRequestResponseDTO>` | 200, 403 |
| POST | `/api/v1/rooms/{roomId}/rental-requests` | `STUDENT` | `RentalRequestCreateDTO` | `RentalRequestResponseDTO` | 201, 400, 403, 404, 409 |
| GET | `/api/v1/my-requests-landlord` | `LANDLORD` | — | `List<RentalRequestResponseDTO>` | 200, 403 |
| PATCH | `/api/v1/rental-requests/{id}/status` | landlord dueño o ADMIN | `RequestStatusUpdateDTO` | `RentalRequestResponseDTO` | 200, 400, 403, 404 |
| POST | `/api/v1/rental-requests/{id}/visits` | estudiante de la solicitud o landlord dueño | `VisitCreateDTO` | `VisitResponseDTO` | 201, 400, 403, 404 |
| GET | `/api/v1/rental-requests/{id}/visits` | involucrados/ADMIN | — | `List<VisitResponseDTO>` | 200, 403, 404 |
| PATCH | `/api/v1/visits/{id}/status` | involucrados | `VisitStatusUpdateDTO` | `VisitResponseDTO` | 200, 400, 403, 404 |
| POST | `/api/v1/rooms/{roomId}/reviews` | `STUDENT` con solicitud confirmada o visita completada | `ReviewCreateDTO` | `ReviewResponseDTO` | 201, 400, 403, 404, 409 |
| GET | `/api/v1/rooms/{roomId}/reviews` | Público | — | `List<ReviewResponseDTO>` | 200, 404 |
| GET | `/api/v1/landlords/{id}/rating` | Público | — | `RatingSummaryDTO` | 200, 404 |
| POST | `/api/v1/me/verification` | `LANDLORD` | `VerificationRequestDTO` (doc) | `VerificationResponseDTO` | 201, 400, 403 |
| GET | `/api/v1/verifications/pending` | `ADMIN` | — | `List<VerificationResponseDTO>` | 200, 403 |
| PATCH | `/api/v1/verifications/{id}` | `ADMIN` | `VerificationDecisionDTO` (approve + comment) | `VerificationResponseDTO` | 200, 400, 403, 404 |

**Dónde se implementan:** `controller/request/RentalRequestController.java` · `controller/visit/VisitController.java` · `controller/review/ReviewController.java` · `controller/verification/VerificationController.java`. Lógica en `service/request/RentalRequestService.java`, `service/visit/VisitService.java`, `service/review/ReviewService.java`, `service/verification/VerificationService.java`.

### C2. Entidades que crea `[issues → #23]` · rama: `feat/iygt8/trust-entities`

**`RentalRequest`** — 📍 `entity/RentalRequest.java`
- *Implementar:* `Long id`, `String message`, `LocalDate startDate`, `LocalDate endDate` (con `@AssertTrue` endDate≥startDate), `RequestStatus status` (`PENDING, CONFIRMED, REJECTED, CANCELLED`), `@ManyToOne Student student`, `@ManyToOne Room room` (**unidireccional**: FK aquí, no se edita la clase de B), `createdAt`.
- *Qué hace:* la solicitud de arriendo; el estado cambia por reglas de transición.
- *Retorno:* registro de solicitud.

**`Visit`** — 📍 `entity/Visit.java`
- *Implementar:* `Long id`, `LocalDateTime scheduledAt` (`@Future`), `notes`, `VisitStatus status` (`PENDING, CONFIRMED, COMPLETED, NO_SHOW`), `@ManyToOne RentalRequest rentalRequest`, `createdAt`.
- *Qué hace:* agenda la visita ligada a una solicitud.
- *Retorno:* registro de visita.

**`Review`** — 📍 `entity/Review.java`
- *Implementar:* `Long id`, `int rating` (`@Min(1) @Max(5)`), `comment` (`@Size(max=500)`), `@ManyToOne Student student`, `@ManyToOne Room room`, `createdAt`, + constraint BD único `(student_id, room_id)` (una reseña por estudiante por habitación).
- *Qué hace:* reseña del estudiante (confianza).
- *Retorno:* registro de reseña.

**`Verification`** — 📍 `entity/Verification.java`
- *Implementar:* `Long id`, `VerificationType type` (`DNI, OWNERSHIP`), `documentReference`, `VerificationStatus status` (`PENDING, APPROVED, REJECTED`), `reviewedBy` (ADMIN), `reviewedAt`, `createdAt`, `@ManyToOne Landlord landlord`.
- *Qué hace:* expediente de verificación manual del landlord.
- *Retorno:* registro de verificación.

### C3. Repositories que crea `[issues → #24]` · rama: `feat/iygt8/trust-repos`

📍 `repository/RentalRequestRepository.java`, `VisitRepository.java`, `ReviewRepository.java`, `VerificationRepository.java`.

| Clase | Métodos | Retorno |
|---|---|---|
| `RentalRequestRepository` | `findByStudent_Id`, `findByRoom_Owner_Id`, `existsByStudent_IdAndRoom_IdAndStatusIn(Set)` (duplicados activos), `findByRoom_IdAndStatus` | `List`/`boolean` |
| `VisitRepository` | `findByRentalRequest_Id`, `findByRentalRequest_Student_Id` | `List<Visit>` |
| `ReviewRepository` | `findByRoom_Id`, `existsByStudent_IdAndRoom_Id`, `@Query("AVG(rating)")` por room y por landlord | `List`/`boolean`/`Double` |
| `VerificationRepository` | `findByStatus(PENDING)`, `findByLandlord_Id` | `List` |

### C4. Servicios que crea `[issues → #26, #27, #28, #29]` · ramas: (mismas de C1)

**`RentalRequestService`** — 📍 `service/request/RentalRequestService.java`
- `create(student, roomId, dto)` — *Implementar:* validar actor `STUDENT` (si no → 403); room existe (404); room `AVAILABLE`; **no existe solicitud activa del mismo estudiante+room** (si existe → `DuplicateResourceException` 409); crear `PENDING`, guardar, **publicar `RentalRequestCreatedEvent`** (notifica al landlord). *Retorno:* `RentalRequestResponseDTO` (201).
- `updateStatus(actor, id, dto)` — *Implementar:* solo landlord del room o ADMIN (403); transiciones `PENDING→CONFIRMED/REJECTED`, `CONFIRMED→CANCELLED` (si no → `InvalidOperationException` 400). *Retorno:* `RentalRequestResponseDTO` (200).
- `getMyRequests(student)` / `getLandlordRequests(landlord)` — *Implementar:* `rentalRequestRepository.findByStudent_Id` / `findByRoom_Owner_Id`. *Retorno:* `List<RentalRequestResponseDTO>` (200).

**`VisitService`** — 📍 `service/visit/VisitService.java`
- `schedule(actor, requestId, dto)` — *Implementar:* actor = estudiante de la solicitud o landlord (403); solicitud en `PENDING`/`CONFIRMED` (si no → `InvalidScheduleException` 400); crear `PENDING`. *Retorno:* `VisitResponseDTO` (201).
- `updateStatus(actor, id, dto)` — *Implementar:* mismos actores; al pasar a `CONFIRMED` **publica `VisitScheduledEvent`**; el estudiante puede marcar `COMPLETED`/`NO_SHOW`. *Retorno:* `VisitResponseDTO` (200).
- `getByRequest(...)` — *Implementar:* `visitRepository.findByRentalRequest_Id`. *Retorno:* `List<VisitResponseDTO>`.

**`ReviewService`** — 📍 `service/review/ReviewService.java`
- `create(student, roomId, dto)` — *Implementar:* estudiante con `RentalRequest` CONFIRMED **o** `Visit` COMPLETED para esa room (si no → `InvalidOperationException` 403); aún no reseñó (`existsByStudent_IdAndRoom_Id` → 409); crear y **recalcular promedio** (AVG por room y por landlord). *Retorno:* `ReviewResponseDTO` (201).
- `getByRoom(roomId)` — *Implementar:* `reviewRepository.findByRoom_Id`. *Retorno:* `List<ReviewResponseDTO>` (200).
- `getLandlordRating(landlordId)` — *Implementar:* `@Query("AVG(rating)")` por landlord + count. *Retorno:* `RatingSummaryDTO` (average + count).

**`VerificationService`** — 📍 `service/verification/VerificationService.java`
- `requestLandlordIdentity(landlord, dto)` — *Implementar:* arrendador sube doc → crea `PENDING`. *Retorno:* `VerificationResponseDTO` (201).
- `getPending()` — *Implementar:* solo `ADMIN` (403 si no). *Retorno:* `List<VerificationResponseDTO>`.
- `decide(admin, id, dto)` — *Implementar:* solo `ADMIN`; al aprobar **setear `Landlord.isVerified = true`** (el badge que muestra B); al rechazar, motivo en `comment`. *Retorno:* `VerificationResponseDTO` (200).

### C5. Eventos, correo y plantillas `[issues → #30, #31]` · ramas: `feat/iygt8/events`, `feat/iygt8/email-service`

📍 `event/RentalRequestCreatedEvent.java`, `event/VisitScheduledEvent.java`, `event/UserRegisteredEventListener.java`, `event/RentalRequestCreatedEventListener.java`, `event/VisitScheduledEventListener.java` · `email/EmailService.java` (`EmailServiceImpl.java`) · `src/main/resources/templates/email/` (`welcome.html`, `request-notification.html`, `visit-confirmation.html`, `reset-password.html`).

- **Eventos:** `RentalRequestCreatedEvent`, `VisitScheduledEvent` (payload del dato creado) + `UserRegisteredEventListener` que escucha el evento de A.
- **`EmailService`** — *Implementar:* `sendHtml(to, subject, template, context)` con `@Async`, JavaMailSender + Thymeleaf; si falla → log sin romper la operación. *Qué hace:* envía correos en segundo plano (usa el `AsyncConfig` de A). *Retorno:* void (asíncrono).
- **Listeners (`@EventListener`):** `UserRegisteredEventListener` → `welcome.html`; `RentalRequestCreatedEventListener` → `request-notification.html` (al landlord); `VisitScheduledEventListener` → `visit-confirmation.html` (estudiante y landlord). *Retorno:* void (efecto colateral: email).

### C6. DTOs que crea `[issues → #25]` · rama: `feat/iygt8/trust-dtos`

📍 `dto/request/` (`RentalRequestCreateDTO`, `RentalRequestResponseDTO`, `RequestStatusUpdateDTO`) · `dto/visit/` (`VisitCreateDTO`, `VisitResponseDTO`, `VisitStatusUpdateDTO`) · `dto/review/` (`ReviewCreateDTO`, `ReviewResponseDTO`) · `dto/verification/` (`VerificationRequestDTO`, `VerificationResponseDTO`, `VerificationDecisionDTO`) · `dto/rating/RatingSummaryDTO`.

- *Implementar:* `RentalRequestCreateDTO` (message, startDate, endDate) · `RentalRequestResponseDTO` (id, room summary, student name, status, fechas, createdAt) · `RequestStatusUpdateDTO` (status) · `VisitCreateDTO` (scheduledAt, notes) · `VisitStatusUpdateDTO` · `ReviewCreateDTO` (rating, comment) · `VerificationRequestDTO`, `VerificationDecisionDTO` (approve + comment) · `RatingSummaryDTO` (average, count).
- *Qué hace:* contratos request/response de la confianza; sin exponer entidades.
- *Retorno:* DTOs con validaciones (`@Valid` en request: `@NotNull`, `@Min/@Max`…).

---

## Verificación final — la suma de las 3 personas = 20 puntos

| Rúbrica | Puntos | Quién lo cubre | Evidencia |
|---|---|---|---|
| §1 Entidades y Modelo (3.0) | 3.0 | A (4) + B (4) + C (4) = **12 entidades**, relaciones JPA, unidireccionales cruzadas, constraints | cumplido |
| §2 DTOs y Mapeo (2.0) | 2.0 | A (~9) + B (~8) + C (~11) = **>20 DTOs** request/response + ModelMapper | cumplido |
| §3 Arquitectura y Patrones (2.0) | 2.0 | Los 3: Controller→Service→Repo, controllers delgados, SRP, DI | cumplido |
| §4 Manejo de Excepciones (2.0) | 2.0 | A: 7+ excepciones + `@ControllerAdvice`; B/C lanzan las suyas | cumplido |
| §5 Seguridad y Autenticación (4.0) | 4.0 | A: SecurityConfig+CORS, JWT (claims+expiración+refresh), roles + `@PreAuthorize`, BCrypt | cumplido |
| §6 API REST y Controllers (2.0) | 2.0 | Los 3: `/api/v1/...`, verbos correctos, códigos (200/201/204/400/401/403/404/409/500) | cumplido |
| §7 Eventos y Asincronía (2.0) | 2.0 | A: `@EnableAsync` + `UserRegisteredEvent`; C: 2 eventos + EmailService `@Async` = **3 casos** | cumplido |
| §8 Deployment (2.0) | 2.0 | Equipo: **AWS EC2 + RDS**, env vars, security groups | cumplido |
| §9 GitHub y Documentación (1.0) | 1.0 | Todos: GitFlow + PRs + Issues/Projects · README (raíz) · postman_collection.json (raíz) | cumplido |
| **TOTAL** | **20.0** | | ✅ |

**Cobertura de la propuesta de negocio:** Registro → A · Publicar habitaciones → B · Buscar por universidad/ubicación/precio → B · Detalle y perfil landlord → B+A · Verificación → C · Calificaciones y reseñas → C · Gestión desde perfil → B+C · **Solicitud de arriendo y visitas** → C · Distancias → B (Haversine) · Notificaciones/email → C. **Todo cubierto.**

**Bonus (no cuentan si excedes 20, pero compensan pérdidas):** Swagger/OpenAPI (C) · SLF4J+Logback (A) · Paginación en listados (B) · Filtros/búsquedas avanzadas (B) · Upload S3 (equipo) · Tests >80% (cada uno en su dominio) · Docker Compose (A) · CI/CD GitHub Actions (equipo).

**Entregables finales:** README.md (1000–2000 pal., 11 secciones) · `postman_collection.json` con variables + auth heredada · repo con `main`/`develop`, `.gitignore`, PRs con review, Issues/Projects con M0–M5.

---

## Dependencias generales y reglas de equipo

- **A empieza el día 1** (todo depende de #2/#3/#5/#11); B y C crean sus entidades el día 1 y quedan **Blocked** solo en sus controllers hasta que exista `SecurityConfig` (#11).
- **Milestones:** M0 Bootstrap (#1–#4) · M1 Seguridad (#5–#13) · M2 Catálogo (#14–#22) · M3 Confianza (#23–#32) · M4 Integración/Deploy (#33–#35) · M5 Docs/QA (#36–#39).
- **GitFlow híbrido:** cada issue = 1 rama `feat/<alias-persona>/<tarea>` desde `develop` → PR a `develop` con review (rúbrica §9.2). Alias: A=`dominguez`, B=`andremejia`, C=`iygt8`. `main` solo de `develop` al terminar hitos.
- **Labels:** `P0-Blocker` / `P1-Alta` / `P2-Normal` / `P3-Bonus` + `foundations` / `rooms` / `trust-flow` / `deploy`. Tablero con columna **Blocked** y campo **"Depende de #"**.
- **Archivos que solo toca A:** `pom.xml`, `application*.yml`, `SecurityConfig`, `GlobalExceptionHandler`.
- **Integración diaria:** pull de `develop` cada mañana; `develop` nunca se rompe a fin de día.

---

## Apéndice A — Mapeo Bloque → Issues

| Sección | Contenido | Issues |
|---|---|---|
| A0 | Bootstrap + informe estructural | #1, #4 |
| A1 | Endpoints auth/usuarios/admin | #10 |
| A2 | Config base | #2, #3 |
| A3 | Entidades base | #5 |
| A4 | Repos base | #6 |
| A5 | Servicios base | #8, #9 |
| A6 | Seguridad | #11 |
| A7 | Excepciones | #12 |
| A8 | Config transversal | #13 |
| A9 | DTOs auth/usuarios + ModelMapper | #7 |
| B1 | Endpoints catálogo | #18, #19, #21, #22 |
| B2 | Entidades catálogo | #14 |
| B3 | Repos catálogo | #15 |
| B4 | Servicios catálogo | #16, #20 |
| B5 | DTOs catálogo | #17 |
| B6 | Regla de seguridad (B) | #18, #19, #21, #22 |
| C1 | Endpoints confianza | #26, #27, #28, #29 |
| C2 | Entidades confianza | #23 |
| C3 | Repos confianza | #24 |
| C4 | Servicios confianza | #26, #27, #28, #29 |
| C5 | Eventos + correo | #30, #31 |
| C6 | DTOs confianza | #25 |
| — | Swagger/OpenAPI (bonus) | #32 |
| — | Integración | #33 |
| — | AWS EC2 + RDS | #34 |
| — | Docker (bonus) | #35 |
| — | README | #36 |
| — | postman_collection.json | #37 |
| — | Checklist rúbrica | #38 |
| — | Tests (bonus) | #39 |

## Apéndice D — Mapa Issue → Sección → Rama

> Cada issue = 1 rama `feat/<alias>/<tarea>` desde `develop` → PR a `develop`. Alias: A=`dominguez`, B=`andremejia`, C=`iygt8`.

| Issue | Sección (md) | Rama |
|---|---|---|
| #1 | A0 | `feat/dominguez/bootstrap` |
| #2 | A2 | `feat/dominguez/spring-setup` |
| #3 | A2 | `feat/dominguez/config-yaml` |
| #4 | A0 | `feat/dominguez/plan-estructural` |
| #5 | A3 | `feat/dominguez/base-entities` |
| #6 | A4 | `feat/dominguez/base-repos` |
| #7 | A9 | `feat/dominguez/auth-dtos` |
| #8 | A5 | `feat/dominguez/auth-service` |
| #9 | A5 | `feat/dominguez/user-services` |
| #10 | A1 | `feat/dominguez/auth-controllers` |
| #11 | A6 | `feat/dominguez/jwt-security` |
| #12 | A7 | `feat/dominguez/exceptions` |
| #13 | A8 | `feat/dominguez/cross-config` |
| #14 | B2 | `feat/andremejia/catalog-entities` |
| #15 | B3 | `feat/andremejia/catalog-repos` |
| #16 | B4 | `feat/andremejia/distance-calculator` |
| #17 | B5 | `feat/andremejia/catalog-dtos` |
| #18 | B1 | `feat/andremejia/university` |
| #19 | B1 | `feat/andremejia/location` |
| #20 | B4 | `feat/andremejia/room-service` |
| #21 | B1 | `feat/andremejia/room-controllers` |
| #22 | B1 | `feat/andremejia/publication` |
| #23 | C2 | `feat/iygt8/trust-entities` |
| #24 | C3 | `feat/iygt8/trust-repos` |
| #25 | C6 | `feat/iygt8/trust-dtos` |
| #26 | C1 | `feat/iygt8/rental-request` |
| #27 | C1 | `feat/iygt8/visit` |
| #28 | C1 | `feat/iygt8/review` |
| #29 | C1 | `feat/iygt8/verification` |
| #30 | C5 | `feat/iygt8/events` |
| #31 | C5 | `feat/iygt8/email-service` |
| #32 | — (bonus) | `feat/iygt8/swagger` |
| #33 | — Integración | `feat/dominguez/integration` |
| #34 | — AWS deploy | `feat/dominguez/aws-deploy` |
| #35 | — Docker (bonus) | `feat/dominguez/docker` |
| #36 | — README | `feat/iygt8/readme` |
| #37 | — Postman | `feat/andremejia/postman` |
| #38 | — Checklist rúbrica | `feat/iygt8/checklist` |
| #39 | — Tests (bonus) | `feat/iygt8/tests` |

## Apéndice B — Activos de las personas

- **SMTP:** credenciales reales (Brevo/Resend) se conectan en M4/M5; en dev el email se simula con `app.mail.enabled=false` (logs).
- **AWS:** se necesita cuenta con tarjeta para EC2 + RDS en M4; si no se consigue, fallback Railway/Render (se avisa antes de M4).
- **Postman:** colección final en la raíz (`postman_collection.json`) con variables de entorno y auth heredada.

## Apéndice C — Licencia

> **© 2026 quédate. Todos los derechos reservados.**
> Proyecto original de la startup quédate. Documentación y código distribuidos con fines académicos para el curso CS2031 — DBP (UTEC, ciclo 2026-2). Prohibida su distribución o uso comercial sin autorización previa de los autores.

