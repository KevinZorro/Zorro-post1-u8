# Zorro-post1-u8 — Sistema de Pedidos con Clean Architecture

API REST de gestión de pedidos implementada con **Spring Boot 3.2**
aplicando los **cuatro círculos concéntricos de Clean Architecture**:
Entities → Use Cases → Interface Adapters → Frameworks & Drivers.

La Dependency Rule se cumple estrictamente: ninguna flecha de dependencia
apunta hacia afuera. El paquete `domain/` no contiene ningún import de
Spring ni de JPA.

---

## Diagrama de Círculos Concéntricos
╔══════════════════════════════════════════════════════════════════╗
║ FRAMEWORKS & DRIVERS (adapter/out/persistence + Spring Boot) ║
║ PedidoJpaEntity PedidoJpaRepository PedidoRepositoryAdapter ║
║ H2 Database PedidoConfiguration (wiring) ║
║ ┌──────────────────────────────────────────────────────────┐ ║
║ │ INTERFACE ADAPTERS (adapter/in/web) │ ║
║ │ PedidoController CrearPedidoRequest PedidoResponse │ ║
║ │ ┌────────────────────────────────────────────────────┐ │ ║
║ │ │ USE CASES (usecase/) │ │ ║
║ │ │ CrearPedidoUseCase ConsultarPedidoUseCase │ │ ║
║ │ │ PedidoRepositoryPort (puerto de salida) │ │ ║
║ │ │ CrearPedidoService ConsultarPedidoService │ │ ║
║ │ │ ┌──────────────────────────────────────────────┐ │ │ ║
║ │ │ │ ENTITIES (domain/) │ │ │ ║
║ │ │ │ Pedido (Aggregate Root) │ │ │ ║
║ │ │ │ PedidoId LineaPedido Dinero EstadoPedido │ │ │ ║
║ │ │ │ java.* únicamente — sin Spring, sin JPA │ │ │ ║
║ │ │ └──────────────────────────────────────────────┘ │ │ ║
║ │ └────────────────────────────────────────────────────┘ │ ║
║ └──────────────────────────────────────────────────────────┘ ║
╚══════════════════════════════════════════════════════════════════╝

Las flechas de dependencia solo apuntan hacia ADENTRO.

---

## Estructura de Paquetes
src/main/java/com/example/cleanpedidos/
├── CleanPedidosApplication.java
├── domain/
│ ├── entity/
│ │ └── Pedido.java ← Aggregate Root (POJO puro)
│ └── valueobject/
│ ├── PedidoId.java ← record UUID tipado
│ ├── Dinero.java ← record BigDecimal validado
│ ├── LineaPedido.java ← record inmutable
│ └── EstadoPedido.java ← enum BORRADOR/CONFIRMADO
├── usecase/
│ ├── CrearPedidoUseCase.java ← interfaz puerto de entrada
│ ├── ConsultarPedidoUseCase.java ← interfaz puerto de entrada
│ ├── port/
│ │ └── PedidoRepositoryPort.java ← interfaz puerto de salida
│ └── impl/
│ ├── CrearPedidoService.java ← lógica pura, sin @Service
│ └── ConsultarPedidoService.java
├── adapter/
│ ├── in/web/
│ │ ├── PedidoController.java ← @RestController
│ │ └── dto/
│ │ ├── CrearPedidoRequest.java
│ │ ├── LineaPedidoDto.java
│ │ └── PedidoResponse.java
│ └── out/persistence/
│ ├── PedidoJpaEntity.java ← @Entity (SOLO aquí)
│ ├── PedidoJpaRepository.java ← JpaRepository
│ └── PedidoRepositoryAdapter.java ← @Component, traduce dominio ↔ JPA
└── config/
└── PedidoConfiguration.java ← @Configuration, wiring explícito

---

## Regla de Dependencias Verificada

| Paquete | Puede importar | NO puede importar |
|---|---|---|
| `domain/` | `java.*` únicamente | Spring, JPA, adaptadores |
| `usecase/` | `domain/` | Spring, JPA, adaptadores |
| `adapter/in/web/` | `usecase/`, `domain/valueobject/` | `adapter/out/` |
| `adapter/out/persistence/` | `usecase/port/`, `domain/` | `adapter/in/` |
| `config/` | Todo (punto de composición) | — |

---

## Requisitos

- Java 17+
- Maven 3.8+

---

## Ejecución

```bash
git clone https://github.com/Kevinzorro/Zorro-post1-u8.git
cd Zorro-post1-u8
mvn spring-boot:run
```

- API: **http://localhost:8080**
- Consola H2: **http://localhost:8080/h2-console**
  - JDBC URL: `jdbc:h2:mem:testdb` | Usuario: `sa` | Contraseña: *(vacía)*

---

## Endpoints REST — `/api/pedidos`

| Método | URL | Descripción | Código OK | Error |
|---|---|---|---|---|
| POST | `/api/pedidos` | Crea y confirma un pedido | 201 | 400 |
| GET | `/api/pedidos` | Lista todos los pedidos | 200 | — |
| GET | `/api/pedidos/{id}` | Busca pedido por UUID | 200 | 404 |

---

## Ejemplos con curl

### Crear pedido → `201 Created`
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteNombre": "Ana García",
    "lineas": [
      {"productoNombre": "Laptop", "cantidad": 1, "precioUnitario": 1500.00},
      {"productoNombre": "Mouse",  "cantidad": 2, "precioUnitario":   25.00}
    ]
  }'
```

### Listar todos → `200 OK`
```bash
curl http://localhost:8080/api/pedidos
```

### Buscar por UUID → `200 OK`
```bash
curl http://localhost:8080/api/pedidos/{pedidoId}
```

### Cliente vacío → `400 Bad Request` (error de dominio)
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"clienteNombre": "", "lineas": [{"productoNombre":"X","cantidad":1,"precioUnitario":10}]}'
```

### Cantidad <= 0 → `400 Bad Request` (error de dominio)
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"clienteNombre": "Carlos", "lineas": [{"productoNombre":"Y","cantidad":0,"precioUnitario":10}]}'
```

---

## Checkpoints Verificados

- [x] `POST /api/pedidos` retorna `201 Created` con `pedidoId` en formato UUID
- [x] `GET /api/pedidos/{id}` retorna el pedido con líneas y total calculado
- [x] `POST` con `clienteNombre` vacío retorna `400` con mensaje de error de dominio
- [x] `POST` con `cantidad <= 0` retorna `400` con mensaje de error de dominio
- [x] `Pedido` instanciable en JUnit sin `@SpringBootTest`
- [x] `CrearPedidoService` testeable con mock de `PedidoRepositoryPort` sin Spring
- [x] `domain/` no contiene imports de `org.springframework` ni `jakarta.persistence`

---

## Diferencia clave entre los tres proyectos (U7 → U8)

| Aspecto | Capas (post1-u7) | Hexagonal (post2-u7) | Clean Arch (post1-u8) |
|---|---|---|---|
| Entidad dominio | `@Entity` JPA | POJO puro | POJO puro con Value Objects |
| Identidad | `Long id` | `String id` | `PedidoId` (record tipado) |
| Validación negocio | `@NotBlank` Bean Val. | En el servicio | En el constructor del Aggregate |
| Wiring | `@Service` automático | `@Configuration` | `@Configuration` explícito |
| Restricción formal | Convención | DIP | Dependency Rule verificable |