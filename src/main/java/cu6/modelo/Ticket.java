package cu6.modelo;

import java.time.LocalDateTime;

/**
 * Ticket que se genera al pagar.
 *
 * @author Gustavo Romero
 */
public class Ticket {

    private final String folio;
    private final Compra compra;
    private final Tarjeta tarjeta;
    private final LocalDateTime fecha;

    public Ticket(String folio, Compra compra, Tarjeta tarjeta) {
        this.folio = folio;
        this.compra = compra;
        this.tarjeta = tarjeta;
        this.fecha = LocalDateTime.now();
    }

    public String getFolio() {
        return folio;
    }

    public Compra getCompra() {
        return compra;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
