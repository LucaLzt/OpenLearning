## ADR-002: Hexagonal Architecture (Ports & Adapters)

### Estado
Aceptado

### Contexto
Queremos que nuestra lógica de negocio (Dominio) no dependa de frameworks (Spring) ni de
bases de datos, para facilitar el mantenimiento y los tests.

### Decisión
Utilizar **Arquitectura Hexagonal (Ports & Adapters)** en cada módulo del monolito modular.

### Consecuencias
* **Ventajas:**
  * El Dominio es puro Java (fácil de testear unitariamente).
  * Podemos cambiar la base de datos o el framework web sin tocar el negocio.

* **Desventajas:**
    * Aumenta la cantidad de clases (DTOs, Mappers, Interfaces de Puertos).