## ADR-005: Testing Strategy with Testcontainers

### Estado
Aceptado

### Contexto
Los tests con bases de datos en memoria (H2) suelen fallar en producción porque H2 no
se comporta igual que PostgreSQL. Necesitamos tests de integración fiables.

### Decisión
Utilizar **TestContainers** para levantar instancias reales de PostgreSQL y MinIO durante
los tests.

### Consecuencias
* **Ventajas:**
    * Tests 100% fidedignos al entorno de producción.
    * Aislamiento total de datos entre tests.
* **Desventajas:**
    * Los tests tardar un poco más en arrancar (Docker startup).