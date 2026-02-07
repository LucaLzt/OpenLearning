# C4 Model - Architecture Overview

> **Propósito del documento:** Este documento describe la arquitectura estática del
> sistema utilizando el modelo C4. Se enfoca en cómo el sistema interactúa con los
> usuarios, sus dependencias externas y su estructura interna modular.

---

## Nivel 1: Diagrama de Contexto (System Context)

*La "foto satelital". Muestra el sistema como una caja negra y sus interacciones con el
mundo exterior.*

```mermaid
graph TD
    %% Estilos
    classDef person fill: #08427b, stroke: #052e56, color: #fff
    classDef system fill: #1168bd, stroke: #0b4884, color: #fff
    classDef external fill: #999, stroke: #666, color: #fff
    %% Actores
    Student(Estudiante):::person
    Instructor(Instructor):::person
    %% Sistema Principal
    LMS("LMS Platform\n[Software System]"):::system
    %% Sistemas Externos
    Email("Email Service\n[External SMTP]"):::external
    MinIO("MinIO / S3\n[Object Storage]"):::external
    %% Relaciones
    Student -->|Busca cursos y aprende| LMS
    Instructor -->|Crea y gestiona cursos| LMS
    LMS -->|Envía correos transaccionales| Email
    LMS -->|Almacena/Recupera Videos e Imágenes| MinIO
```

### Descripción

* **LMS Platform:** El núcleo de nuestra solución.
* **Email Service:** Servicio externo (o servidor SMTP) para notificaciones de bienvenida
  y recuperación de contraseña.
* **MinIO / S3:** Almacenamiento de objetos desacoplado para gestionar archivos binarios
  pesados (Videos de lecciones, Portadas).

---

## Nivel 2: Diagrama de Contenedores (Container)

*Zoom-in a la infraestructura. Muestra las unidades desplegables y tecnologías elegidas.*

```mermaid
graph TD
    %% Estilos
    classDef user fill: #08427b, stroke: #052e56, color: #fff
    classDef spa fill: #1168bd, stroke: #0b4884, color: #fff
    classDef api fill: #2f9bc1, stroke: #1f7a9e, color: #fff
    classDef db fill: #999, stroke: #666, color: #fff
    %% Actores
        User(Usuario Generico):::user
    %% Contenedores
    subgraph Docker Host
        WebApp("Single Page Application\n[React + Vite]"):::spa
        Backend("API Application\n[Java 21 + Spring Boot]"):::api
        Database("Primary Database\n[PostgreSQL]"):::db
        BlobStore("Object Storage\n[MinIO]"):::db
        Cache("Redis Cache\n[In-Memory Key-Value Store]"):::db
    end

    %% Relaciones
    User -->|Navega via HTTPS| WebApp
    WebApp -->|JSON / REST API| Backend
    Backend -->|SQL / JDBC| Database
    Backend -->|AWS SDK / HTTP| BlobStore
    Backend -->|Lectura/Escritura rápida| Cache
```

### Decisiones Clave

1. **Single Page Application (SPA):** El frontend está totalmente desacoplado del backend,
   permitiendo despliegues independientes de la UI.
2. **Spring Boot API:** Actúa como el orquestador central y "Monolito Modular".
3. **MinIO Local:** Se utiliza una instancia contenerizada de MinIO para simular AWS S3
   en el entorno de desarrollo y MVP.
4. **PostgreSQL:** Base de datos relacional robusta y ampliamente soportada.

---

## Nivel 3: Diagrama de Componentes (Modular Monolith Structure)

*Zoom-in al Backend. Aquí se evidencia la arquitectura **Spring Modulith***

En lugar de capas horizontales (Controller -> Service -> Repository), visualizamos
**Módulos Verticales** encapsulados.

```mermaid
graph TD
    %% Estilos
    classDef core fill: #2f9bc1, stroke: #1f7a9e, color: #fff
    classDef module fill: #1168bd, stroke: #0b4884, color: #fff, stroke-dasharray: 5 5

    subgraph "API Application (Spring Boot)"
    %% Módulos
        Identity("[Módulo: Identity\n(Auth, Users, Roles)]"):::module
        Catalogue("[Módulo: Catalogue\n(Search, Public Info)]"):::module
        Enrollment("[Módulo: Enrollment\n(Purchases, Access)]"):::module
        Content("[Módulo: Content\n(Course Mgmt, Videos)]"):::module
    %% Base de Datos (Lógica)
        DB[(Shared Database)]:::core
    end

    %% Relaciones entre módulos (Simplificado)
    Enrollment -->|Valida usuario| Identity
    Content -->|Publica curso| Catalogue
    Enrollment -->|Verifica curso| Catalogue
    %% Todos tocan la DB (en esquemas separados idealmente)
    Identity -.-> DB
    Catalogue -.-> DB
    Enrollment -.-> DB
    Content -.-> DB
```

### Arquitectura Interna

* **Separación Lógica:** Aunque todo corre en un solo proceso Java (`.jar`), el código
  está organizado en paquetes raíz separados.
* **Comunicación:** Los módulos se comunican principalmente a través de:
    1. **Eventos de Dominio** (Asíncrono - Ej.: `CoursePublishedEvent`).
    2. **Interfaces Públicas** (Síncrono - Ej.: `IdentityService` expuesto).
* **Aislamiento:** Cada módulo tiene su propia capa de persistencia interna, inaccesible
  para los otros módulos directamente.