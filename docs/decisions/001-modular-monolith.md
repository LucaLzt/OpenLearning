## ADR-001: Modular Monolith Architecture
 
### Estado
Aceptado

### Contexto
Necesitamos construir una plataforma escalable, pero somos un equipo pequeño (uno solo).
La latencia de red y la complejidad de despliegue de los Microservicios son riesgos altos.

### Decisión
Implementar un **Monolito Modular** que permita separar las responsabilidades en módulos
usando **Spring Modulith**.

### Consecuencias
* **Ventajas:**
  * Despliegue simple (un solo .jar).
  * Límites claros entre módulos (No "código espagueti").
  * Posibilidad de extraer un módulo a Microservicio en el futuro sin reescribir todo.

* **Desventajas:**
  * Requiere disciplina estricta para no acoplar módulos.