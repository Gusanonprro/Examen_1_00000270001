package cu6.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Productos que lleva el cliente.
 *
 * @author Gustavo Romero
 */
public class Compra {

    private final List<RenglonCompra> renglones = new ArrayList<>();

    public List<RenglonCompra> getRenglones() {
        return Collections.unmodifiableList(renglones);
    }

    // si el producto ya estaba se suma la cantidad
    public void agregar(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        RenglonCompra renglon = buscarRenglon(producto);
        int cantidadTotal = cantidad + (renglon == null ? 0 : renglon.getCantidad());
        if (!producto.hayExistencia(cantidadTotal)) {
            throw new IllegalStateException("Solo hay " + producto.getExistencia() + " pieza(s) de "
                    + producto.getNombre() + ".");
        }
        if (renglon == null) {
            renglones.add(new RenglonCompra(producto, cantidad));
        } else {
            renglon.agregarCantidad(cantidad);
        }
    }

    public int getTotalArticulos() {
        int total = 0;
        for (RenglonCompra renglon : renglones) {
            total += renglon.getCantidad();
        }
        return total;
    }

    // precios con IVA incluido
    public double getTotal() {
        double total = 0;
        for (RenglonCompra renglon : renglones) {
            total += renglon.getSubtotal();
        }
        return total;
    }

    public boolean estaVacia() {
        return renglones.isEmpty();
    }

    private RenglonCompra buscarRenglon(Producto producto) {
        for (RenglonCompra renglon : renglones) {
            if (renglon.getProducto() == producto) {
                return renglon;
            }
        }
        return null;
    }
}
