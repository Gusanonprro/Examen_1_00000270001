package observador;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para los modelos que avisan cuando cambian.
 *
 * @author Gustavo Romero
 */
public abstract class Observable {

    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    // avisa a todos los observadores
    protected void notificarObservadores() {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.actualizar();
        }
    }
}
