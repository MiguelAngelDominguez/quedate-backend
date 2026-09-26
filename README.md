# Quédate — Backend

**Curso:** CS 2031 Desarrollo Basado en Plataforma
**Universidad:** UTEC
**Ciclo:** 2026-2

## Integrantes

- **[NOMBRE COMPLETO]** — @MiguelAngelDominguez — Fundación, seguridad y usuarios
- **[NOMBRE COMPLETO]** — @andremejia-hub — Catálogo y búsqueda de habitaciones
- **[NOMBRE COMPLETO]** — @iygt8-iterate — Confianza, solicitudes y comunicación

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

**Quédate** es una plataforma orientada a estudiantes universitarios que necesitan encontrar habitaciones cercanas a su universidad de una manera más organizada y confiable.

El proyecto se enfoca principalmente en estudiantes de UTEC y busca centralizar información que normalmente se encuentra dispersa en redes sociales, grupos de mensajería, publicaciones informales o contactos personales.

El objetivo principal del backend es proporcionar una API que permita gestionar usuarios, arrendadores, habitaciones, ubicaciones, solicitudes de alquiler, visitas, reseñas y procesos de verificación. Además, se incorporan mecanismos de autenticación, autorización por roles, eventos y notificaciones por correo electrónico.

La aplicación fue desarrollada utilizando una arquitectura por capas con Spring Boot y una API REST versionada bajo el prefijo `/api/v1`.

---

## 2. Identificación del problema

Buscar alojamiento cerca de una universidad puede convertirse en un proceso complicado para un estudiante que no conoce bien la zona o que debe trasladarse grandes distancias todos los días.

Actualmente, buena parte de las ofertas de habitaciones se publican en medios generales o informales. Esto puede generar problemas como:

- Información fragmentada entre diferentes plataformas.
- Dificultad para comparar precios, ubicaciones y características.
- Falta de mecanismos para validar a los arrendadores.
- Falta de un flujo estructurado para solicitar una habitación.
- Coordinación manual de visitas.
- Reseñas realizadas sin comprobar una experiencia real.
- Escasa información sobre la cercanía de una habitación a la universidad.

Solucionar este problema resulta relevante porque el alojamiento puede afectar directamente el tiempo de transporte, los gastos y la experiencia universitaria de un estudiante.

Quédate busca proporcionar un flujo centralizado desde la búsqueda de una habitación hasta la interacción con el arrendador.

---

## 3. Descripción de la solución

El backend de Quédate implementa distintos módulos que cubren las principales necesidades del sistema.

Los usuarios pueden registrarse y autenticarse. Dependiendo de su rol, pueden actuar como estudiantes, arrendadores o administradores.

Los arrendadores pueden gestionar habitaciones y publicaciones. Las habitaciones contienen información como título, descripción, precio, capacidad, tamaño, imágenes, ubicación, universidad relacionada y estado de disponibilidad.

Los estudiantes pueden consultar habitaciones y utilizar criterios como universidad, distrito y rango de precios para realizar búsquedas.

El módulo de confianza agrega funcionalidades adicionales:

- Solicitudes de alquiler.
- Estados y transiciones de solicitudes.
- Programación de visitas.
- Confirmación y seguimiento de visitas.
- Reseñas de habitaciones.
- Cálculo de calificaciones.
- Verificación manual de arrendadores.
- Eventos de dominio.
- Correos transaccionales.

Para permitir una reseña, el estudiante debe tener una experiencia relacionada con la habitación, por ejemplo una solicitud confirmada o una visita completada. De esta manera se busca reducir reseñas sin relación real con el alojamiento.

---

## 4. Tecnologías utilizadas

El proyecto utiliza las siguientes tecnologías:

- **Java 26**
- **Spring Boot 4.1.x**
- **Maven**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **JWT**
- **Jakarta Validation**
- **PostgreSQL**
- **H2**
- **Lombok**
- **ModelMapper**
- **JavaMailSender**
- **Thymeleaf**
- **Spring Actuator**

Durante el desarrollo se dispone de un perfil que utiliza H2 en memoria.

Para producción, el proyecto está preparado para utilizar PostgreSQL mediante variables de entorno y una instancia administrada en AWS RDS.

El correo electrónico utiliza JavaMailSender y plantillas Thymeleaf.

---

## 5. Arquitectura del backend

El proyecto sigue una arquitectura por capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Los **controllers** reciben las solicitudes HTTP, procesan parámetros y DTO, aplican validaciones y delegan la lógica.

Los **services** contienen las reglas de negocio. En esta capa se realizan validaciones como disponibilidad de habitaciones, permisos de actores, transiciones de estado, comprobación de solicitudes previas y verificación de experiencias.

Los **repositories** utilizan Spring Data JPA para interactuar con la base de datos.

Los **DTO** permiten separar las entidades persistidas de los contratos enviados o recibidos por la API.

La estructura principal del proyecto incluye:

```text
config/
controller/
dto/
email/
entity/
event/
exception/
repository/
security/
service/
util/
```

Esta separación facilita la mantenibilidad y permitió dividir el trabajo entre los integrantes del equipo.

---

## 6. Modelo de entidades

El proyecto utiliza múltiples entidades JPA relacionadas con las diferentes áreas del sistema.

Entre las principales se encuentran:

- **User:** información principal del usuario y autenticación.
- **Role:** roles y permisos.
- **Student:** perfil especializado del estudiante.
- **Landlord:** perfil del arrendador.
- **University:** universidad asociada al sistema.
- **Location:** información de ubicación.
- **Room:** habitación ofrecida.
- **Publication:** publicación relacionada con una habitación.
- **RentalRequest:** solicitud de alquiler.
- **Visit:** visita programada.
- **Review:** reseña y calificación.
- **Verification:** proceso de verificación de un arrendador.

Las relaciones utilizan anotaciones JPA como `@OneToOne`, `@ManyToOne` y `@ManyToMany`.

Por ejemplo, una solicitud de alquiler pertenece a un estudiante y a una habitación. Una visita pertenece a una solicitud. Una reseña relaciona a un estudiante con una habitación.

Las reseñas incluyen una restricción única para evitar que un mismo estudiante registre múltiples reseñas sobre la misma habitación.

El diseño detallado y la distribución de entidades se documentan también en `PLAN_ESTRUCTURAL.md`.

---

## 7. API REST

La API utiliza el prefijo:

```text
/api/v1
```

Algunos de sus endpoints principales son:

### Autenticación y usuarios

```text
POST  /api/v1/auth/register
POST  /api/v1/auth/login
POST  /api/v1/auth/refresh
GET   /api/v1/users/me
PUT   /api/v1/users/me
GET   /api/v1/students/{id}
PUT   /api/v1/landlords/me/profile
GET   /api/v1/admin/users
```

### Solicitudes de alquiler

```text
POST   /api/v1/rooms/{roomId}/rental-requests
GET    /api/v1/my-rental-requests
GET    /api/v1/my-requests-landlord
PATCH  /api/v1/rental-requests/{id}/status
```

### Visitas

```text
POST   /api/v1/rental-requests/{id}/visits
GET    /api/v1/rental-requests/{id}/visits
PATCH  /api/v1/visits/{id}/status
```

### Reseñas

```text
POST  /api/v1/rooms/{roomId}/reviews
GET   /api/v1/rooms/{roomId}/reviews
GET   /api/v1/landlords/{id}/rating
```

### Verificación

```text
POST   /api/v1/me/verification
GET    /api/v1/verifications/pending
PATCH  /api/v1/verifications/{id}
```

El backend utiliza códigos HTTP como `200`, `201`, `400`, `401`, `403`, `404`, `409` y `500` según el resultado de cada operación.

Los controllers buscan mantenerse delgados y delegar las reglas de negocio a los services.

---

## 8. Manejo de errores

El proyecto contempla excepciones específicas para representar errores de negocio.

Entre los casos considerados se encuentran:

- Recursos no encontrados.
- Recursos duplicados.
- Credenciales incorrectas.
- Operaciones inválidas.
- Acceso no autorizado.
- Acceso prohibido por permisos.
- Tokens inválidos o expirados.
- Horarios o estados inválidos.

La arquitectura contempla un manejo global de excepciones para traducir estos errores a respuestas HTTP consistentes.

El formato general de error puede incluir:

```text
timestamp
status
error
message
path
```

El objetivo es evitar exponer directamente excepciones internas de Java al consumidor de la API y proporcionar respuestas más claras y predecibles.

---

## 9. Seguridad y autenticación

La seguridad del backend está basada en Spring Security y JWT.

Después de realizar el login, el cliente utiliza el token de acceso mediante:

```text
Authorization: Bearer <token>
```

La arquitectura contempla generación y validación de JWT, expiración de tokens, refresh tokens y claims relacionados con el usuario y sus roles.

Las contraseñas se almacenan utilizando BCrypt y no deben formar parte de los DTO enviados al cliente.

El control de autorización se realiza mediante roles y anotaciones como:

```java
@PreAuthorize("hasRole('ADMIN')")
```

También existen operaciones reservadas para estudiantes o arrendadores.

Las credenciales y secretos del sistema no deben almacenarse directamente en el repositorio. La aplicación utiliza variables de entorno para valores sensibles como configuración de base de datos, JWT, SMTP y CORS.

---

## 10. Eventos, asincronía y correo

El proyecto utiliza eventos para desacoplar las operaciones principales de tareas secundarias como las notificaciones.

Entre los eventos utilizados se encuentran:

- `RentalRequestCreatedEvent`
- `VisitScheduledEvent`
- `UserRegisteredEvent`

Los listeners utilizan `@EventListener`.

Por ejemplo, el servicio que crea una solicitud no necesita encargarse directamente de enviar un correo. Publica un evento y otro componente puede reaccionar al evento.

El servicio de correo dispone del método:

```text
sendHtml(to, subject, template, context)
```

y utiliza JavaMailSender y Thymeleaf.

Las plantillas HTML desarrolladas son:

```text
welcome.html
request-notification.html
visit-confirmation.html
reset-password.html
```

El método de envío utiliza `@Async` para evitar que una operación de correo bloquee la respuesta principal de la API.

El comportamiento del correo se controla mediante:

```text
app.mail.enabled
```

Cuando el servicio de correo se encuentra desactivado, el sistema registra la situación y evita interrumpir la operación principal.

---

## 11. Ejecución del proyecto

### Requisitos

Para ejecutar el proyecto se requiere:

- IntelliJ IDEA
- JDK 26
- Maven
- Git

### Ejecución mediante IntelliJ IDEA

1. Clonar el repositorio.
2. Abrir IntelliJ IDEA.
3. Ir a **File → Open**.
4. Seleccionar la carpeta raíz del proyecto.
5. Esperar a que IntelliJ detecte el archivo `pom.xml`.
6. Configurar **Java 26** como SDK del proyecto.
7. Permitir que Maven descargue e indexe las dependencias.
8. Seleccionar el perfil de desarrollo cuando corresponda.
9. Ejecutar la clase `QuedateApplication`.

La clase principal se encuentra en:

```text
src/main/java/com/quedate/QuedateApplication.java
```

También puede ejecutarse mediante Maven, si Maven se encuentra instalado:

```bash
mvn spring-boot:run
```

Para desarrollo se utiliza H2.

Las variables configurables se encuentran documentadas en:

```text
.env.example
```

Entre las variables contempladas se encuentran:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
JWT_EXPIRATION
REFRESH_EXPIRATION
SMTP_HOST
SMTP_PORT
SMTP_USERNAME
SMTP_PASSWORD
CORS_ALLOWED_ORIGINS
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

El equipo utiliza Git y GitHub para dividir y controlar el trabajo.

Cada funcionalidad se desarrolla en una rama independiente siguiendo un esquema similar a:

```text
feat/<integrante>/<tarea>
```

Las ramas se crean a partir de `develop` y posteriormente se integran mediante Pull Requests.

La rama `main` se reserva para versiones estables e integradas.

El proyecto se organizó en tres áreas principales:

- **A:** fundación, autenticación, seguridad y usuarios.
- **B:** catálogo, habitaciones, ubicaciones y búsqueda.
- **C:** confianza, solicitudes, visitas, reseñas, verificación, eventos y correo.

GitHub Issues se utiliza para asignar tareas y representar dependencias entre funcionalidades. Los milestones organizan el avance desde el bootstrap del proyecto hasta integración, deployment, documentación y QA.

El archivo `PLAN_ESTRUCTURAL.md` documenta la distribución de responsabilidades, clases y módulos.

El repositorio también contempla integración y automatización mediante GitHub Actions dentro de las tareas de integración del proyecto. Su estado final depende de la etapa de integración y QA correspondiente.

---

## 14. Conclusiones

Quédate busca resolver un problema concreto: facilitar a los estudiantes la búsqueda y gestión de alojamiento cercano a su universidad.

El backend integra funcionalidades de autenticación, catálogo de habitaciones, ubicación, solicitudes, visitas, reseñas, verificación y comunicación.

Durante el desarrollo se aplicaron conceptos de arquitectura por capas, diseño REST, Spring Data JPA, DTO, validaciones, seguridad, JWT, autorización por roles, manejo de errores, eventos y asincronía.

También fue importante utilizar Git mediante ramas independientes, issues y Pull Requests, ya que permitió que varios integrantes trabajaran en paralelo sobre diferentes módulos.

Como trabajo futuro se pueden considerar mejoras como almacenamiento de imágenes en AWS S3, incremento de la cobertura de pruebas, automatización completa del deployment mediante CI/CD, monitoreo en producción y expansión del servicio a otras universidades.

---

## 15. Apéndices

### Archivos principales

```text
README.md
PLAN_ESTRUCTURAL.md
FLUJO_DE_RECOMENDACION.md
.env.example
pom.xml
src/main/resources/application.yml
src/main/resources/application-dev.yml
```

### Licencia

```text
[PENDIENTE: indicar la licencia definitiva del proyecto]
```

### Referencias tecnológicas

- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2
- AWS
- Thymeleaf
  - JSON Web Token (JWT)