## ADR-003: Authentication & Authorization Strategy

### Estado
Aceptado

### Contexto
La aplicación necesita manejar sesiones de forma segura y escalable. Las sesiones en
servidor (Stateful) dificultan el escalado horizontal y el uso de aplicaciones móviles.

### Decisión
Usar **JWT (Stateless) para acceso corto y Refresh Tokens (Stateful/Rotativos) para
sesiones largas**.
Se implementará un mecanismo de **Token Blacklisting** utilizando **Redis**. Redis fue seleccionado por
su baja latencia (O(1)) y su capacidad nativa de definir expiración (TTL) en las claves, lo que permite
que la lista negra se 'autolimpie'.

### Consecuencias
* **Ventajas:**
    * Frontend tiene control de la sesión.
    * Base de datos no se satura validando cada request (solo valida al refrescar).
    * Permite revocación de acceso (Logout) mediante invalidación del Refresh Token.
* **Desventajas:**
  * Aumenta la complejidad de la infraestructura al requerir un contenedor de Redis adicional. Introduce
  un punto de fallo: Si Redis cae, el mecanismo de logout inmediato deja de funcionar.