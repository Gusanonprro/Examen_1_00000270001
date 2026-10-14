# Examen_1_00000270001 – Diseño de Software (Primera Unidad de Competencia)

Caso de uso asignado: **CU6 – Comprar producto**.

Proyecto Java (Maven) para NetBeans implementado con el patrón **MVC + Observer** y coherente con el modelo de
diseño del archivo `Examen_1_00000270001.mdj` (StarUML).

## Arquitectura MVC + Observer

El paquete `observador` contiene la interfaz `Observador` (`actualizar()`) y la clase
abstracta `Observable` (`agregarObservador`, `eliminarObservador`, `notificarObservadores`).

1. La **vista** (`FrmCompra`, extiende `JFrame` e implementa `Observador`) recibe el evento del usuario y llama al controlador.
2. El **controlador** (`ControlCompra`) invoca la operación del modelo; si el modelo la rechaza, muestra el error con `vista.mostrarMensaje(...)`.
3. El **modelo** (`Tienda`, extiende `Observable`) cambia su estado y llama a `notificarObservadores()`.
4. La vista, registrada por el controlador con `modelo.agregarObservador(vista)`, recibe `actualizar()`,
   consulta el estado del modelo con sus métodos `get…`/`obtener…` y se redibuja.

## Cómo ejecutar

1. NetBeans → *File → Open Project* → seleccionar la carpeta `Examen_1_00000270001`.
2. *Run Project* (F6) abre la ventana del caso de uso (`Examen` → `cu6.Main`).
3. Requiere JDK 17 o superior.

## Clases

| Paquete | Clases |
|---|---|
| `cu6.modelo` | `Producto`, `RenglonCompra`, `Compra`, `Tarjeta`, `Ticket`, `Tienda` |
| `cu6.vista` | `FrmCompra` |
| `cu6.controlador` | `ControlCompra` |
| `cu6` | `Main` (incluye productos y tarjetas de prueba) |
| `observador` | `Observador`, `Observable` |

Reglas: la cantidad no puede exceder la existencia (si se repite un producto se suman las cantidades), precios con IVA
incluido, la tarjeta debe tener 16 dígitos y estar registrada (de ella salen el banco emisor y la ciudad; bancos ficticios),
el número se muestra enmascarado, folio consecutivo `VTA-2026-0001`. Tarjeta de prueba: `4152 3138 0000 1234`.
