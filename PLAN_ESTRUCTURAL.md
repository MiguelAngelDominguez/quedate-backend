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

> Los números de issues hacen referencia a la numeración real del repositorio GitHub.

---

## Persona A — Todo lo que le toca `[issues → #5, #6, #7, #8, #9, #10, #11, #12, #13]`

### A1. Endpoints que implementa `[issues → #10]`

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

### A2. Clase principal y configuración base `[issues → #2, #3]`

**`QuedateApplication.java`** — 📍 `src/main/java/com/quedate/QuedateApplication.java` · clase `@SpringBootApplication` con `main`.

**`pom.xml`** — 📍 raíz del proyecto · Spring Boot `4.1.1` (parent) · Java 26 · dependencias: Web, Data JPA, Security, Validation, Mail, Thymeleaf, Actuator, H2 (dev), PostgreSQL (prod), Lombok, ModelMapper, jjwt (api/impl/jackson), starter-test, security-test. **Regla: solo A lo modifica; B/C piden dependencias por issue.**

**`application.yml`** + perfiles — 📍 `src/main/resources/application.yml` · `application-dev.yml` (H2 en memoria) · `application-prod.yml` (PostgreSQL vía variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `REFRESH_EXPIRATION`, `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `CORS_ALLOWED_ORIGINS`).

**`.gitignore`** — 📍 raíz · excluye `target/`, `.idea/`, `*.iml`, `.env`, `application-prod.yml`, logs. **`.env.example`** — 📍 raíz · plantilla de variables sin valores reales.

### A3. Entidades (tablas) que crea `[issues → #5]` (+ `DataSeeder` en #13)

**`User`** — 📍 `src/main/java/com/quedate/entity/User.java` · `Long id`, `firstName`, `lastName`, `email` (`@Column(nullable=false, unique=true)` + `@NotBlank` + `@Email`), `password` (hash BCrypt), `phone`, `@ManyToMany(fetch=EAGER) Set<Role> roles`, `createdAt`, `updatedAt`. **El password nunca se serializa en DTOs.**

**`Role`** — 📍 `entity/Role.java` · `Long id`, `@Enumerated(EnumType.STRING) RoleName name` donde `RoleName = {USER, LANDLORD, ADMIN, MANAGER}`.

**`Student`** — 📍 `entity/Student.java` · `Long id`, `@OneToOne User user`, `String dni` (unique, 8 dígitos), `program`, `entryYear`, `scholarshipCode` (opcional).

**`Landlord`** — 📍 `entity/Landlord.java` · `Long id`, `@OneToOne User user`, `dni` (unique), `bio`, `boolean isVerified = false`, `createdAt`. `isVerified` lo actualiza el servicio de Verificación de C.

**`DataSeeder`** (`CommandLineRunner`) — 📍 `config/DataSeeder.java` · precarga los 4 roles, un ADMIN demo (`admin@quedate.com`), la universidad **UTEC – Miraflores** (coordinar con B) y 2-3 rooms demo.

### A4. Repositories que crea `[issues → #6]`

📍 `src/main/java/com/quedate/repository/UserRepository.java`, `RoleRepository.java`, `StudentRepository.java`, `LandlordRepository.java`.

| Clase | Métodos | Qué hace | Retorno |
|---|---|---|---|
| `UserRepository` | `findByEmail(String)` | busca por email (login) | `Optional<User>` |
| `UserRepository` | `existsByEmail(String)` | valida unicidad al registrar | `boolean` |
| `RoleRepository` | `findByName(RoleName)` | carga el rol | `Optional<Role>` |
| `StudentRepository` | `findByUserId(Long)` | perfil por usuario | `Optional<Student>` |
| `LandlordRepository` | `findByUserId(Long)`, `existsByDni(String)` | perfil landlord y DNI único | `Optional` / `boolean` |

### A5. Servicios que crea `[issues → #8, #9]`

📍 `service/auth/AuthService.java`, `service/user/UserService.java`, `service/user/StudentService.java`, `service/user/LandlordService.java`.

**`AuthService`**
- `register(RegisterRequestDTO dto)` — ① `existsByEmail` → `EmailAlreadyExistsException` (409); ② password débil → `InvalidOperationException` (400); ③ **BCrypt**; ④ crea `User` + `Role` + su `Student`/`Landlord`; ⑤ guarda; ⑥ **publica `UserRegisteredEvent`**. Retorno: `AuthResponseDTO` (201).
- `login(LoginRequestDTO dto)` — valida credenciales contra `UserDetailsServiceImpl`; falla → `BadCredentialsException` (401); genera access token (claims: userId, email, roles) y refresh token. Retorno: `AuthResponseDTO` (200).
- `refresh(RefreshTokenRequestDTO dto)` — valida firma/expiración del refresh; inválido → `InvalidTokenException` (401); emite access nuevo. Retorno: `AuthResponseDTO` (200).
- `getCurrentUser()` — lee `SecurityContext` → `UserPrincipal`. Retorno: `UserResponseDTO` (sin password).

**`UserService`** — `getById(id)` → 404 si no (`ResourceNotFoundException`); `update(id, dto)`; lista admin con paginación → `Page<UserResponseDTO>`.
**`StudentService`** — `getProfileByUserId(id)` → `StudentResponseDTO`; `update(...)`.
**`LandlordService`** — `getProfileByUserId`, `updateProfile(actor, dto)` (solo dueño, si no → `ForbiddenException`), `getById` → `LandlordResponseDTO`.

### A6. Seguridad (todo el motor) `[issues → #11]`

📍 `config/SecurityConfig.java` · `security/JwtService.java` · `security/JwtAuthenticationFilter.java` · `security/UserDetailsServiceImpl.java` · `security/UserPrincipal.java`.

- **`SecurityConfig`** — bean `SecurityFilterChain`: `.csrf(disable)`, sesiones `STATELESS`, `.httpBasic(disable)`, entry point JSON 401, `.cors()` con `CorsConfigurationSource`, públicas: `"/api/v1/auth/**"`, `GET /api/v1/rooms/**`, `GET /api/v1/universities/**`, `GET /api/v1/locations/**`, Swagger; resto `.authenticated()`; filtro JWT antes de `UsernamePasswordAuthenticationFilter`. `@EnableMethodSecurity`.
- **`JwtService`** — `generateToken(UserPrincipal)` → `String`; `generateRefreshToken(...)` → `String`; `extractUserId/Email/Roles(token)` → claims; `isValid/isExpired` → `boolean`. Firman con `JWT_SECRET`.
- **`JwtAuthenticationFilter`** (`OncePerRequestFilter`) — extrae `Bearer`, valida, carga `UserDetails` y setea `SecurityContext`.
- **`UserDetailsServiceImpl`** — `loadUserByUsername(email)` → `UserPrincipal` o `UsernameNotFoundException` (401).
- **`UserPrincipal`** — envuelve `User`: `getUserId()`, `getEmail()`, `getAuthorities()` (roles), password hash.

### A7. Excepciones y respuesta de error `[issues → #12]`

📍 `src/main/java/com/quedate/exception/` · `GlobalExceptionHandler.java` · `dto/ErrorResponseDTO.java`.
- **7 excepciones:** `ResourceNotFoundException` (404), `DuplicateResourceException` (409), `EmailAlreadyExistsException` (409, extiende la anterior), `InvalidOperationException` (400), `UnauthorizedException` (401), `ForbiddenException` (403), `InvalidTokenException` (401). (B y C agregan `RoomNotAvailableException.java` e `InvalidScheduleException.java`.)
- **`GlobalExceptionHandler`** (`@ControllerAdvice`) — captura personalizadas + `MethodArgumentNotValidException` + `HttpMessageNotReadableException` + fallback 500.
- **`ErrorResponseDTO`** — `timestamp, status, error, message, path`; formato único para toda la API.

### A8. Config transversal `[issues → #13]`

📍 `config/AsyncConfig.java` (`@EnableAsync` + `ThreadPoolTaskExecutor` core 4 max 8) · `config/ModelMapperConfig.java` (bean `ModelMapper`, strict) · `event/UserRegisteredEvent.java` (payload `User`, publicado por `AuthService.register`; listener lo consume C).

---

## Persona B — Todo lo que le toca `[issues → #14, #15, #16, #17, #18, #19, #20, #21, #22]`

### B1. Endpoints que implementa `[issues → #18, #19, #21, #22]`

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

### B2. Entidades que crea `[issues → #14]`

**`University`** — 📍 `entity/University.java` · `Long id`, `name` (`@NotNull`), `campusName`, `address`, `Double latitude`, `Double longitude`, `website`. Dato esperado en el Seeder (con A): UTEC, Campus Miraflores, coords reales.

**`Location`** — 📍 `entity/Location.java` · `Long id`, `district` (`@NotNull`), `address`, `Double latitude`, `Double longitude`, `Double distanceToUniversityKm` (se calcula).

**`Room`** — 📍 `entity/Room.java` · `Long id`, `title` (`@NotBlank`, `@Size(max=100)`), `description`, `BigDecimal price` (`@NotNull`, `@Min(1)`), `Integer capacity` (`@Min(1)`), `Double sizeM2`, `@ElementCollection List<String> images`, `RoomStatus status` (`AVAILABLE`/`RENTED`), `boolean isVerified = false`, `@ManyToOne Landlord owner`, `@ManyToOne University university`, `@ManyToOne Location location`, `createdAt`, `updatedAt`. Dueño = LANDLORD; `isVerified` lo actualiza C.

**`Publication`** — 📍 `entity/Publication.java` · `Long id`, `@ManyToOne Room room`, `PublicationStatus status` (`ACTIVE`/`ARCHIVED`), `publishedAt`, `archivedAt`.

### B3. Repositories que crea `[issues → #15]`

📍 `repository/RoomRepository.java`, `UniversityRepository.java`, `LocationRepository.java`, `PublicationRepository.java`.

| Clase | Método clave | Qué hace | Retorno |
|---|---|---|---|
| `RoomRepository` | `findByOwner_Id(Long)` | rooms de un landlord | `List<Room>` |
| `RoomRepository` | `search(...)` `@Query`/Specification (`universityId`, `district`, `minPrice`, `maxPrice`, `status=AVAILABLE`) + `Pageable` | búsqueda filtrada | `Page<Room>` |
| `UniversityRepository` | `findByNameContainingIgnoreCase`, `findAll` | búsqueda/lista | `List` |
| `LocationRepository` | `findByDistrictIgnoreCase(String)` | filtrar por distrito | `List<Location>` |
| `PublicationRepository` | `findByRoom_Id`, `findByStatus` | publicaciones | `List<Publication>` |

### B4. Servicios que crea `[issues → #16, #20]`

📍 `service/room/RoomService.java`, `service/university/UniversityService.java`, `service/location/LocationService.java`, `util/DistanceCalculator.java`.

**`RoomService`**
- `create(actor, RoomCreateDTO)` — valida price>0 y capacity≥1; crea `Room` con el LANDLORD autenticado. Retorno: `RoomDetailDTO` (201).
- `update(actor, id, dto)` — si no es dueño ni ADMIN → `ForbiddenException`. Retorno: `RoomDetailDTO`.
- `delete(actor, id)` — misma validación. Retorno: void (204).
- `getById(id)` — 404 si no existe. Retorno: `RoomDetailDTO`.
- `search(RoomSearchFilterDTO, Pageable)` — filtros + paginación + orden por cercanía (Haversine) y precio. Retorno: `Page<RoomSummaryDTO>`.
- `getByLandlord(id)` / `getMyRooms(actor)` — Retorno `List<RoomSummaryDTO>`.
- `publish(actor, roomId)` / `archive(...)` — crea `Publication`. Retorno: `PublicationDTO`.

**`UniversityService`** — `list()` → `List<UniversityDTO>`; `getById` (404); `create(ADMIN)` → (201).
**`LocationService`** — `list(district?)` → `List<LocationDTO>`; `create(ADMIN)`.
**`DistanceCalculator`** — 📍 `util/DistanceCalculator.java` · `haversine(lat1, lng1, lat2, lng2)` → `double` km (cercanía al campus sin pagar Google Maps).

### B5. DTOs que crea `[issues → #17]`

📍 `dto/room/` (`RoomCreateDTO`, `RoomUpdateDTO`, `RoomDetailDTO`, `RoomSummaryDTO`, `RoomSearchFilterDTO`) · `dto/university/` (`UniversityDTO`, `UniversityCreateDTO`) · `dto/location/LocationDTO` · `dto/publication/PublicationDTO`.

- `RoomCreateDTO`: title, description, price, capacity, sizeM2, images[], locationId, universityId.
- `RoomUpdateDTO`: mismos (opcionales) + status.
- `RoomDetailDTO`: todo lo anterior + id, ownerName, district, universityName, averageRating (lo llena C), isVerified, distanceKm.
- `RoomSummaryDTO`: id, title, price, primera imagen, district, universityName, distanceKm.
- `RoomSearchFilterDTO`: minPrice, maxPrice, district, universityId, page, size, sortBy.

### B6. Regla de seguridad que aplica `[issues → #18, #19, #21, #22]`

`@PreAuthorize("hasRole('LANDLORD')")` y `hasRole('ADMIN')` **en cada método del controller** (`RoomController.java`, `UniversityController.java`); verificación de "es el dueño" (con `getCurrentUser()`) **dentro del service**. No se toca el `SecurityConfig` de A.

---

## Persona C — Todo lo que le toca `[issues → #23, #24, #25, #26, #27, #28, #29, #30, #31, #32]`

### C1. Endpoints que implementa `[issues → #26, #27, #28, #29]`

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

### C2. Entidades que crea `[issues → #23]`

**`RentalRequest`** — 📍 `entity/RentalRequest.java` · `Long id`, `message`, `LocalDate startDate`, `endDate` (`@AssertTrue` endDate≥startDate), `RequestStatus status` (`PENDING, CONFIRMED, REJECTED, CANCELLED`), `@ManyToOne Student student`, `@ManyToOne Room room` (**unidireccional**: FK aquí, no se edita la clase de B), `createdAt`.

**`Visit`** — 📍 `entity/Visit.java` · `Long id`, `LocalDateTime scheduledAt` (`@Future`), `notes`, `VisitStatus status` (`PENDING, CONFIRMED, COMPLETED, NO_SHOW`), `@ManyToOne RentalRequest rentalRequest`, `createdAt`.

**`Review`** — 📍 `entity/Review.java` · `Long id`, `int rating` (`@Min(1) @Max(5)`), `comment` (`@Size(max=500)`), `@ManyToOne Student student`, `@ManyToOne Room room`, `createdAt`, constraint BD único `(student_id, room_id)`.

**`Verification`** — 📍 `entity/Verification.java` · `Long id`, `VerificationType type` (`DNI, OWNERSHIP`), `documentReference`, `VerificationStatus status` (`PENDING, APPROVED, REJECTED`), `reviewedBy` (ADMIN), `reviewedAt`, `createdAt`, `@ManyToOne Landlord landlord`.

### C3. Repositories que crea `[issues → #24]`

📍 `repository/RentalRequestRepository.java`, `VisitRepository.java`, `ReviewRepository.java`, `VerificationRepository.java`.

| Clase | Métodos | Retorno |
|---|---|---|
| `RentalRequestRepository` | `findByStudent_Id`, `findByRoom_Owner_Id`, `existsByStudent_IdAndRoom_IdAndStatusIn(Set)` (duplicados activos), `findByRoom_IdAndStatus` | `List`/`boolean` |
| `VisitRepository` | `findByRentalRequest_Id`, `findByRentalRequest_Student_Id` | `List<Visit>` |
| `ReviewRepository` | `findByRoom_Id`, `existsByStudent_IdAndRoom_Id`, `@Query("AVG(rating)")` por room y por landlord | `List`/`boolean`/`Double` |
| `VerificationRepository` | `findByStatus(PENDING)`, `findByLandlord_Id` | `List` |

### C4. Servicios que crea `[issues → #26, #27, #28, #29]`

📍 `service/request/RentalRequestService.java`, `service/visit/VisitService.java`, `service/review/ReviewService.java`, `service/verification/VerificationService.java`.

**`RentalRequestService`**
- `create(student, roomId, dto)` — actor `STUDENT` (si no → 403); room existe (404); room `AVAILABLE`; **no existe solicitud activa del mismo estudiante+room** (si existe → 409); crea `PENDING`, guarda, **publica `RentalRequestCreatedEvent`**. Retorno: `RentalRequestResponseDTO` (201).
- `updateStatus(actor, id, dto)` — solo landlord del room o ADMIN (403); transiciones `PENDING→CONFIRMED/REJECTED`, `CONFIRMED→CANCELLED` (si no → 400). Retorno: `RentalRequestResponseDTO`.
- `getMyRequests(student)` / `getLandlordRequests(landlord)` — Retorno: `List<RentalRequestResponseDTO>`.

**`VisitService`**
- `schedule(actor, requestId, dto)` — actor = estudiante de la solicitud o landlord (403); solicitud en `PENDING`/`CONFIRMED` (si no → `InvalidScheduleException` 400); crea `PENDING`. Retorno: `VisitResponseDTO` (201).
- `updateStatus(actor, id, dto)` — mismos actores; al pasar a `CONFIRMED` **publica `VisitScheduledEvent`**; el estudiante puede marcar `COMPLETED`/`NO_SHOW`. Retorno: `VisitResponseDTO`.
- `getByRequest(...)` — Retorno: `List<VisitResponseDTO>`.

**`ReviewService`**
- `create(student, roomId, dto)` — estudiante con `RentalRequest` CONFIRMED o `Visit` COMPLETED (si no → 403); aún no reseñó (→ 409); crea y **recalcula promedio**. Retorno: `ReviewResponseDTO` (201).
- `getByRoom(roomId)` — Retorno: `List<ReviewResponseDTO>`.
- `getLandlordRating(landlordId)` — Retorno: `RatingSummaryDTO` (average + count).

**`VerificationService`**
- `requestLandlordIdentity(landlord, dto)` — sube doc → `PENDING`. Retorno: `VerificationResponseDTO` (201).
- `getPending()` — solo ADMIN (403). Retorno: `List<VerificationResponseDTO>`.
- `decide(admin, id, dto)` — solo ADMIN; al aprobar **setea `Landlord.isVerified = true`**. Retorno: `VerificationResponseDTO`.

### C5. Eventos, correo y plantillas `[issues → #30, #31]`

📍 `event/RentalRequestCreatedEvent.java`, `event/VisitScheduledEvent.java`, `event/UserRegisteredEventListener.java`, `event/RentalRequestCreatedEventListener.java`, `event/VisitScheduledEventListener.java` · `email/EmailService.java` (`EmailServiceImpl.java`) · `src/main/resources/templates/email/` (`welcome.html`, `request-notification.html`, `visit-confirmation.html`, `reset-password.html`).

- **Eventos:** `RentalRequestCreatedEvent`, `VisitScheduledEvent` (payload del dato creado) + `UserRegisteredEventListener` que escucha el evento de A.
- **`EmailService`** — `sendHtml(to, subject, template, context)` con **`@Async`**, JavaMailSender + Thymeleaf; si falla → log sin romper la operación. Retorno: void.
- **Listeners:** `UserRegisteredEventListener` → `welcome.html`; `RentalRequestCreatedEventListener` → `request-notification.html` (al landlord); `VisitScheduledEventListener` → `visit-confirmation.html` (estudiante y landlord).

### C6. DTOs que crea `[issues → #25]`

📍 `dto/request/` (`RentalRequestCreateDTO`, `RentalRequestResponseDTO`, `RequestStatusUpdateDTO`) · `dto/visit/` (`VisitCreateDTO`, `VisitResponseDTO`, `VisitStatusUpdateDTO`) · `dto/review/` (`ReviewCreateDTO`, `ReviewResponseDTO`) · `dto/verification/` (`VerificationRequestDTO`, `VerificationResponseDTO`, `VerificationDecisionDTO`) · `dto/rating/RatingSummaryDTO`.

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
| A1 | Endpoints auth/usuarios/admin | #10 |
| A2 | Config base | #2, #3 |
| A3 | Entidades base | #5 |
| A4 | Repos base | #6 |
| A5 | Servicios base | #8, #9 |
| A6 | Seguridad | #11 |
| A7 | Excepciones | #12 |
| A8 | Config transversal | #13 |
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
| — | Integración | #33 |
| — | AWS EC2 + RDS | #34 |
| — | Docker (bonus) | #35 |
| — | README | #36 |
| — | postman_collection.json | #37 |
| — | Checklist rúbrica | #38 |
| — | Tests (bonus) | #39 |

## Apéndice B — Activos de las personas

- **SMTP:** credenciales reales (Brevo/Resend) se conectan en M4/M5; en dev el email se simula con `app.mail.enabled=false` (logs).
- **AWS:** se necesita cuenta con tarjeta para EC2 + RDS en M4; si no se consigue, fallback Railway/Render (se avisa antes de M4).
- **Postman:** colección final en la raíz (`postman_collection.json`) con variables de entorno y auth heredada.

## Apéndice C — Licencia

> **© 2026 quédate. Todos los derechos reservados.**
> Proyecto original de la startup quédate. Documentación y código distribuidos con fines académicos para el curso CS2031 — DBP (UTEC, ciclo 2026-2). Prohibida su distribución o uso comercial sin autorización previa de los autores.