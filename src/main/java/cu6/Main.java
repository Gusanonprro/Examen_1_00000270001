package cu6;

import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import cu6.controlador.ControlCompra;
import cu6.modelo.Producto;
import cu6.modelo.Tarjeta;
import cu6.modelo.Tienda;
import cu6.vista.FrmCompra;

/**
 * CU6 Comprar producto.
 *
 * @author Gustavo Romero
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // se queda el look and feel por defecto
        }
        SwingUtilities.invokeLater(() -> {
            Tienda modelo = new Tienda(productosDePrueba(), tarjetasDePrueba());
            FrmCompra vista = new FrmCompra(modelo);
            ControlCompra control = new ControlCompra(modelo, vista);
            control.iniciar();
        });
    }

    // datos de prueba
    private static List<Producto> productosDePrueba() {
        return List.of(
                new Producto("P01", "Laptop 14 pulgadas", 12499.00, 5),
                new Producto("P02", "Mouse inalámbrico", 349.00, 20),
                new Producto("P03", "Teclado mecánico", 1299.00, 10),
                new Producto("P04", "Monitor 24 pulgadas", 3199.00, 6),
                new Producto("P05", "Audífonos con micrófono", 899.00, 12),
                new Producto("P06", "Memoria USB 64 GB", 189.00, 30),
                new Producto("P07", "Disco SSD 1 TB", 1549.00, 8),
                new Producto("P08", "Cámara web HD", 749.00, 2));
    }

    // tarjetas registradas (prueba: 4152 3138 0000 1234)
    private static List<Tarjeta> tarjetasDePrueba() {
        return List.of(
                new Tarjeta("4152313800001234", "BBVA", "Cd. Obregón, Sonora"),
                new Tarjeta("5204168800005678", "Banco del Bienestar", "Hermosillo, Sonora"),
                new Tarjeta("4915669900009012", "NU Bank", "Navojoa, Sonora"));
    }
}
