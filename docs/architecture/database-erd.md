# Database Entity Relationship Diagram (ERD)

> **Propósito del documento:** Este documento detalla el esquema de base de datos para el MVP. El diseño 
> sigue el patrón **Database-per-Module** (lógico), utilizando esquemas de PostgreSQL o prefijos de tabla
> para aislar los contextos.

**Nota de Arquitectura:** Las relaciones entre tablas de distintos módulos son **Lógicas (Soft References)**,
no físicas. No se utilizan Foreign Keys (FK) entre módulos para permitir la futura extracción a microservicios.

## 1. Módulo: Identity (Seguridad)
*Prefijo sugerido: `ident_` o schema `identity`*

Encargado de usuarios y sesiones. No sabe nada de cursos ni compras.

```mermaid
erDiagram
    %% --- IDENTITY CONTEXT ---
    USERS {
        uuid id PK
        varchar email UK "Unique"
        varchar password_hash
        varchar full_name
        varchar roles "Enum: STUDENT, INSTRUCTOR"
        datetime created_at
    }
    
    REFRESH_TOKENS {
        uuid id PK
        uuid user_id FK "Hard FK a Users"
        varchar token_hash
        varchar device_info "User Agent / OS"
        datetime expires_at
        boolean revoked
        uuid parent_token_id "Para rotación y detección de robo"
    }
    
    USERS ||--o{ REFRESH_TOKENS : has_sessions
    
    REDIS_KEY_VALUE {
        varchar key "blacklist:token:{jti}"
        varchar value "true"
        int ttl "Tiempo restante de expiración del JWT"
    }
```
> *Nota:* La tabla `REDIS_KEY_VALUE` se utiliza para almacenar claves efímeras de tokens revocados. La
> expiración (TTL) se ajusta automáticamente al tiempo de vida restante del JWT.

## 2. Módulo: Content (Gestión Educativa)
*Prefijo sugerido: `cont_` o schema `content`*

Donde los instructores crean la estructura. Aquí vive la "verdad" del contenido.

```mermaid
erDiagram
    %% --- CONTENT CONTEXT ---
    COURSES {
        uuid id PK
        uuid instructor_id "Soft Ref a Identity.User"
        varchar title
        varchar status "Enum: DRAFT, PUBLISHED"
        decimal price
        varchar coverImageUrl
        datetime last_updated
    }

    SECTIONS {
        uuid id PK
        uuid course_id FK "Hard FK a Courses"
        varchar title
        int order_index
    }

    LESSONS {
        uuid id PK
        uuid section_id FK "Hard FK a Sections"
        varchar title
        varchar video_url "MinIO Key / YouTube ID"
        text content_text
        int order_index
    }

    COURSES ||--|{ SECTIONS: contains
    SECTIONS ||--|{ LESSONS: contains
```

## 3. Módulo: Catalogue (Vidrieda Pública)
*Prefijo sugerido: `cat_` o schema `catalogue`*

Optimizado para lectura rápida. Es una proyección de los datos del curso.

```mermaid
erDiagram
    %% --- CATALOGUE CONTEXT ---
    COURSE_PRODUCTS {
        uuid id PK "Mismo UUID que Content.Course"
        varchar title
        varchar description
        decimal price
        varchar instructor_name "Desnormalizado para no consultar Identity"
        varchar cover_image_url
        datetime published_at
    }
```
* **Nota:** Esta tabla se llena escuchando el evento `CoursePublishedEvent` del módulo Content.

## 4. Módulo: Enrollment (Inscripciones)
*Prefijo sugerido: `enr_` o schema `enrollment`*

Registro transaccional de acceso.

```mermaid
erDiagram
    %% --- ENROLLMENT CONTEXT ---
    ENROLLMENTS {
        uuid id PK
        uuid user_id "Soft Ref a Identity.User"
        uuid course_id "Soft Ref a Content.Course"
        datetime enrollment_date
        string status "Enum: ACTIVE, REFUNDED"
        decimal amount_paid
    }
```
