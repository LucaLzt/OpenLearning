## ADR-004: Blob Storage with MinIO

### Estado
Aceptado

### Contexto
El sistema debe almacenar videos e imágenes. Guardar binarios (BLOBs) en la base de datos
SQL es ineficiente y encarece los backups. Usar el disco local del servidor impide escalar
(si agrego otro servidor, no ve los archivos del primero).

### Decisión
Utilizar **MinIO** como almacenamiento de objetos compatible con S3.

### Consecuencias
* **Ventajas:**
    * Compatibilidad nativa con AWS S3 (migración a la nube transparente en el futuro).
    * Base de datos SQL ligera (solo guarda la URL/Key del archivo).
* **Desventajas:**
  * Requiere gestionar un contenedor extra en la infraestructura (Docker).