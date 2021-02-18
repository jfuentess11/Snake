package principal;

/**
 * Clase Crónometro
 * 
 * Encargada de contar el tiempo de la partida
 * 
 * @author Javier Fuentes
 */
public class Cronometro extends Thread {
    private long tiempoInicial;
    private long tiempoTranscurrido;

    @Override
    public void run() {
        tiempoInicial = System.nanoTime();
        while (true) {
            tiempoTranscurrido = System.nanoTime() - tiempoInicial;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Devuelve el tiempo transcurrido desde que empezó el hilo
     * @return long con el tiempo transcurrido
     */
    public long getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
}
