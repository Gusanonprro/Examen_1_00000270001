package cu6.modelo;

/**
 * Tarjeta registrada para pagar.
 *
 * @author Gustavo Romero
 */
public class Tarjeta {

    private final String numero;
    private final String bancoEmisor;
    private final String ciudad;

    public Tarjeta(String numero, String bancoEmisor, String ciudad) {
        if (numero == null || !numero.matches("\\d{16}")) {
            throw new IllegalArgumentException("El número de tarjeta debe tener 16 dígitos.");
        }
        this.numero = numero;
        this.bancoEmisor = bancoEmisor;
        this.ciudad = ciudad;
    }

    public String getNumero() {
        return numero;
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public String getCiudad() {
        return ciudad;
    }

    // solo muestra los ultimos 4 digitos
    public String getNumeroEnmascarado() {
        return "**** **** **** " + numero.substring(12);
    }
}
