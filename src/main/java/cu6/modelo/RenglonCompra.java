package cu6.modelo;

/**
 * Producto con su cantidad dentro de la compra.
 *
 * @author Gustavo Romero
 */
public class RenglonCompra {

    private final Producto producto;
    private int cantidad;

    public RenglonCompra(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void agregarCantidad(int cantidad) {
        this.cantidad += cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
