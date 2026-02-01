## ADR-003: Authentication & Authorization Strategy

### Estado
Aceptado

### Contexto
La aplicación necesita manejar sesiones de forma segura y escalable. Las sesiones en
servidor (Stateful) dificultan el escalado horizontal y el uso de aplicaciones móviles.

### Decisión
Usar **JWT (Stateless) para acceso corto y Refresh Tokens (Stateful/Rotativos) para
sesiones largas**.

### Consecuencias
* **Ventajas:**
    * Frontend tiene control de la sesión.
    * Base de datos no se satura validando cada request (solo valida al refrescar).
    * Permite revocación de acceso (Logout) mediante invalidación del Refresh Token.