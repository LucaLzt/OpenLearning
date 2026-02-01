# Diccionario de Dominio (Lenguaje Ubicuo)

> **Propósito del documento:** Definir los términos clave del negocio, agrupados por **Contexto Delimitado
(Bounded Context)**. El objetivo es desambiguar términos que pueden tener nombres iguales pero
responsabilidades diferentes.

---

## 1. Contexto: Identidad (Identity Context)
_Encargado de: Autenticación, Seguridad y Roles._

* **Usuario (User):** Entidad que representa a una persona registrada en el sistema. Posee credenciales de acceso.
* **Credenciales (Credentials):** Datos sensibles (Email y Password hasheado) utilizados para autenticar a un usuario.
* **Rol (Role):** Permiso de alto nivel que define qué puede hacer un usuario (Ej.: `ROLE_STUDENT`, `ROLE_INSTRUCTOR`).
* **Token de Refresco (Refresh Token):** Credencial de larga duración y alta seguridad que representa una
**Sesión Activa** en un dispositivo específico.
  * _Responsabilidad:_ Permite renovar el acceso (Obtener nuevos JWTs) sin pedir credenciales nuevamente.
  * *Información de Contexto:* Almacena metadata del cliente (User-Agent, IP, Sistema Operativo) para identificar *dónde* 
  está abierta la sesión.
  * *Ciclo de Vida:* Nace al hacer login, rota con cada uso (Rotación de Tokens) y muere al hacer logout o expirar.

---

## 2. Contexto: Catálogo (Catalogue Context)
_Encargado de: Visualización pública, Búsqueda y "Vidriera"._

* **Curso (Course):** Aquí, un curso es un **Producto Digital** que se exhibe.
  * _Datos clave:_ Título, Subtítulo, Precio actual, Imagen de portada, Nombre del Instructor.
  * _Nota:_ En este contexto, el curso **NO** tiene videos ni lecciones, solo **metadatos** para la venta.
* **Instructor (Public Profile):** La representación pública del creador del curso. Solo nos interesa su Nombre,
Foto y Biografía breve.
* **Búsqueda (Search Query):** Intención del usuario de encontrar cursos filtrando por texto.

---

## 3. Contexto: Inscripciones (Enrollment Context)
_Encargado de: Transacciones y Permisos de acceso._

* **Inscripción (Enrollment):** El registro de que un **Estudiante** ha adquirido el derecho de acceder
a un **Curso**.
  * _Estados:_ `PENDIENTE`, `ACTIVA`, `CANCELADA`.
* **Estudiante (Student):** El usuario visto desde la perspectiva de "comprador". Nos interesa su historial
de compras, no su perfil social.

---

## 4. Contexto: Contenido & Lecciones (Content Context)
_Encargado de: Estructura educativa, Edición y Consumo._

* **Curso (Course):** Aquí, un curso es una **Estructura Pedagógica**.
  * _Composición:_ Es un contenedor de **Secciones**.
  * _Estado:_ `BORRADOR`, `PUBLICADO`.
* **Sección (Section):** Agrupador lógico de lecciones (Ej.: "Módulo 1: Introducción").
* **Lección (Lesson):** La unidad mínima de contenido educativo.
  * _Tipos:_ Video (URL externa), Texto/Artículo.
* **Progreso (Progress):** (Futuro/V2) Marca de qué lecciones ha completado un estudiante específico.