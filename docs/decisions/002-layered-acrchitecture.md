## ADR-002: Layered Architecture

### Estado

Aceptado

### Contexto

Necesitamos una estructura organizativa para el código que sea eficiente y fácil de entender. La
Arquitectura Hexagonal propuesta inicialmente introduce una complejidad accidental (exceso de
Mappers, DTOs intermedios y Puertos) que no se justifica para la etapa actual del proyecto (MVP),
donde la velocidad de entrega es prioritaria.

### Decisión

Utilizar una **Arquitectura en Capas (Layered Architecture)** tradicional dentro de cada módulo
del monolito.
El flujo de dependencias será: `Web (Controller) -> Service (Bussiness Logic) -> Access (Repository)`.

### Consecuencias

* **Ventajas:**
    * Simplicidad: Reducción drástica de código "boilerplate" (menos interfaces y conversores
      de datos).
    * Velocidad: Desarrollo más ágil aprovechando las convenciones estándar de Spring Boot.
    * Curva de Aprendizaje: Es el patrón arquitectónico más común y fácil de adoptar para
      desarrolladores Java.

* **Desventajas:**
    * Acoplamiento: La lógica de negocio tiende a conocer detalles de la persistencia (ej. usando
      Entidades JPA directamente en el Servicio).
    * Menor Aislamiento: Cambiar el framework o la base de datos a futuro podría requerir
      refactorizar la capa de servicio.