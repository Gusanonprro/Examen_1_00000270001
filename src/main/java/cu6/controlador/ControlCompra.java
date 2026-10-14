package cu6.controlador;

import cu6.modelo.Producto;
import cu6.modelo.Tienda;
import cu6.vista.FrmCompra;

/**
 * Controlador de la compra.
 *
 * @author Gustavo Romero
 */
public class ControlCompra {

    private final Tienda modelo;
    private final FrmCompra vista;

    public ControlCompra(Tienda modelo, FrmCompra vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vista.setControl(this);
        this.modelo.agregarObservador(vista);
    }

    public void iniciar() {
        vista.actualizar();
        vista.setVisible(true);
    }

    public void agregarProducto(Producto producto, int cantidad) {
        try {
            modelo.agregarProducto(producto, cantidad);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            // no hay existencia o la cantidad no es valida
            vista.mostrarMensaje(ex.getMessage());
        }
    }

    public void validarTarjeta(String numero) {
        String limpio = numero.replaceAll("[\\s-]", "");
        if (!limpio.matches("\\d{16}")) {
            // la tarjeta debe tener 16 digitos
            vista.mostrarMensaje("El número de tarjeta debe tener 16 dígitos.");
            return;
        }
        modelo.consultarTarjeta(limpio);
    }

    public void pagar() {
        try {
            modelo.pagar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarMensaje(ex.getMessage());
        }
    }
}
