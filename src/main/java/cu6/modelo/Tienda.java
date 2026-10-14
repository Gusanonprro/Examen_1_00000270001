package cu6.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import observador.Observable;

/**
 * Modelo principal de la tienda.
 *
 * @author Gustavo Romero
 */
public class Tienda extends Observable {

    private final List<Producto> productos;
    private final List<Tarjeta> tarjetas;
    private Compra compraActual;
    private String numeroTarjetaBuscado;
    private Tarjeta tarjetaActual;
    private Ticket ticketActual;
    private int consecutivoFolio;

    public Tienda(List<Producto> productos, List<Tarjeta> tarjetas) {
        this.productos = new ArrayList<>(productos);
        this.tarjetas = new ArrayList<>(tarjetas);
        this.compraActual = new Compra();
        this.consecutivoFolio = 0;
    }

    public List<Producto> obtenerProductos() {
        return Collections.unmodifiableList(productos);
    }

    public Compra obtenerCompra() {
        return compraActual;
    }

    public String getNumeroTarjetaBuscado() {
        return numeroTarjetaBuscado;
    }

    public Tarjeta getTarjetaActual() {
        return tarjetaActual;
    }

    public Ticket getTicketActual() {
        return ticketActual;
    }

    // agrega un producto a la compra
    public void agregarProducto(Producto producto, int cantidad) {
        compraActual.agregar(producto, cantidad);
        ticketActual = null;
        notificarObservadores();
    }

    // busca la tarjeta, si no existe queda en null
    public void consultarTarjeta(String numero) {
        numeroTarjetaBuscado = numero == null ? "" : numero.replaceAll("[\\s-]", "");
        tarjetaActual = null;
        for (Tarjeta tarjeta : tarjetas) {
            if (tarjeta.getNumero().equals(numeroTarjetaBuscado)) {
                tarjetaActual = tarjeta;
            }
        }
        notificarObservadores();
    }

    // realiza el pago y genera el ticket
    public void pagar() {
        if (compraActual.estaVacia()) {
            throw new IllegalStateException("No hay productos seleccionados.");
        }
        if (tarjetaActual == null) {
            throw new IllegalArgumentException("Primero consulte la tarjeta de pago.");
        }
        for (RenglonCompra renglon : compraActual.getRenglones()) {
            renglon.getProducto().descontarExistencia(renglon.getCantidad());
        }
        ticketActual = new Ticket(generarFolio(), compraActual, tarjetaActual);
        compraActual = new Compra();
        tarjetaActual = null;
        numeroTarjetaBuscado = null;
        notificarObservadores();
    }

    // folio tipo VTA-2026-0001
    private String generarFolio() {
        consecutivoFolio++;
        return String.format("VTA-2026-%04d", consecutivoFolio);
    }
}
