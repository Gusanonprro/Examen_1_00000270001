package cu6.modelo;

/**
 * Producto de la tienda.
 *
 * @author Gustavo Romero
 */
public class Producto {

    private final String clave;
    private final String nombre;
    private final double precio;
    private int existencia;

    public Producto(String clave, String nombre, double precio, int existencia) {
        if (precio <= 0 || existencia < 0) {
            throw new IllegalArgumentException("Precio o existencia inválidos.");
        }
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
    }

    public String getClave() {
        return clave;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public boolean hayExistencia(int cantidad) {
        return cantidad > 0 && cantidad <= existencia;
    }

    public void descontarExistencia(int cantidad) {
        if (!hayExistencia(cantidad)) {
            throw new IllegalStateException("No hay existencia suficiente de " + nombre + ".");
        }
        existencia -= cantidad;
    }
}
