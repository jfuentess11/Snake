package pantallas;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import principal.Cronometro;
import principal.PanelJuego;
import principal.Pantalla;
import principal.Sprite;

/**
 * Pantalla de Juego
 * 
 * @author Javier Fuentes
 */
public class PantallaJuego implements Pantalla {

    private float SEGUNDOS_HILO = 0.2f;
    // Referencia al Panel de Juego
    private PanelJuego panelJuego;
    // CONSTANTES SERPIENTE
    private int LADO_SERPIENTE = 20;
    private String RUTA_CABEZA_DOWN = "Imagenes/cabezaDown.png";
    private String RUTA_CABEZA_UP = "Imagenes/cabezaUp.png";
    private String RUTA_CABEZA_LEFT = "Imagenes/cabezaLeft.png";
    private String RUTA_CABEZA_RIGHT = "Imagenes/cabezaRight.png";
    private String RUTA_CUERPO = "Imagenes/cuerpo.png";
    // CONSTANTES MANZANA
    private int LADO_MANZANA = 20;
    private String RUTA_MANZANA = "Imagenes/manzana.png";
    // CONSTANTE ALTO DEL PANEL INFORMATIVO
    private int ALTO_PANEL_SUPERIOR = 40;
    // Fondo de pantalla
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;
    // Sprites de la pantalla
    private Sprite manzana;
    private ArrayList<Sprite> serpiente;
    
    // Variables de control del juego
    private String direccion;
    private static int puntuacion;
    private boolean finJuego = false;
    private boolean haTecleado = false;
    // Cronometro
    private Cronometro cronometro;
    // Formato del tiempo
    private DecimalFormat formatoTiempo;

    // Constructor
    public PantallaJuego(PanelJuego panelJuego) {
        this.panelJuego = panelJuego;
        formatoTiempo = new DecimalFormat("#");
    }

    @Override
    public void inicializarPantalla() {
        // Inicia la serpiente y le añade el primer Sprite que será la cabeza
        serpiente = new ArrayList<Sprite>();
        serpiente.add(new Sprite(RUTA_CABEZA_DOWN, LADO_SERPIENTE, LADO_SERPIENTE * 2, LADO_SERPIENTE * 4));
        // Inicia la manzana
        manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, LADO_MANZANA * 5, LADO_MANZANA * 6);
        // La serpietnte se empeará a mover hacía abajo
        direccion = "DOWN";
        // Inicia la puntuación a 0
        puntuacion = 0;
        // Carga el fondo de pantalla
        try {
            fondo = ImageIO.read(new File("Imagenes/fondo.jpg"));
        } catch (Exception e) {
        }
        // Fuerza el redimensionamiento de pantalla para cambiar la imagen de fondo
        redimensionarPantalla(null);
        // Inicia el cronómetro
        cronometro = new Cronometro();
        cronometro.start();
    }

    @Override
    public void pintarPantalla(Graphics g) {
        // Pinta el fondo
        rellenarFondo(g);
        // Escribe la puntuación
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Puntuación: "+ (puntuacion<10?"0":"")+ puntuacion + "/50", 10, 30);
        // Escribe el Tiempo
        g.drawString("Tiempo:", 300, 30);
        g.drawString(formatoTiempo.format(cronometro.getTiempoTranscurrido()/1e9), 420, 30);
        // pinta una linea que separará el tablero de juego del panel de información
        g.drawRect(0, 0, panelJuego.getWidth(), ALTO_PANEL_SUPERIOR);
        // Pinta la manzana
        if (manzana != null) {
            manzana.estampar(g);
        }
        // Pinta la serpiente
        for (Sprite sprite : serpiente) {
            sprite.estampar(g);
        }

    }

    @Override
    public void rellenarFondo(Graphics g) {
        g.drawImage(fondoRedimensionado, 0, 0, null);
    }

    @Override
    public void ejecutarFrame() {
        // si la manzana no exite crea una
        if (manzana == null) {
            crearManzanaFueraDeLaSerpiente();
        } else {
            // Reinicia para que pueda vover a pulsar una tecla de movimiento
            haTecleado = false;
            // si ya hay manzana sigue la ejecución
            try {
                Thread.sleep(Long.parseLong(""+ (int)(SEGUNDOS_HILO * 1000)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // EL panel pide el foco para que funcione la detección de teclas para moverse
            panelJuego.requestFocus();
            moverSerpiente();

            combrobarColisiones();

            if (finJuego) {
                pasarDePantalla(new PantallaDerrota(panelJuego));
            }
        }
    }

    /**
     * Método encargado de comprobar el siguiente movimiento de la serpiente y de
     * moverla
     */
    private void moverSerpiente() {
        for (int i = serpiente.size() - 1; i >= 0; i--) {
            if (i == 0) {
                calcularMovimientoCabeza();
            } else {
                int movXAnterior = serpiente.get(i - 1).getVelX();
                int movYAnterior = serpiente.get(i - 1).getVelY();
                serpiente.get(i).setVelX(movXAnterior);
                serpiente.get(i).setVelY(movYAnterior);
            }
        }

        for (Sprite sprite : serpiente) {
            sprite.mover();
        }
    }

    /**
     * Este método combrobará las posibles colisiones de la serpiente
     */
    private void combrobarColisiones() {
        // Comprueba la si la serpiente colisiona consigo misma
        if (serpiente.size() > 4) {
            for (int i = 0; i < serpiente.size() - 1; i++) {
                for (int j = i + 4; j < serpiente.size(); j++) {
                    if (serpiente.get(i).colisiona(serpiente.get(j))) {
                        finJuego = true;
                    }
                }
            }
        }
        // Comprueba si la serpiene colisiona con un límite del panel
        if (serpiente.get(0).getPosX() + LADO_SERPIENTE > panelJuego.getWidth()) {
            finJuego = true;
        } else if (serpiente.get(0).getPosX() < 0) {
            finJuego = true;
        } else if (serpiente.get(0).getPosY() < ALTO_PANEL_SUPERIOR) {
            finJuego = true;
        } else if (serpiente.get(0).getPosY() + LADO_SERPIENTE > panelJuego.getHeight()) {
            finJuego = true;
        }
        // Comprueba si la cabeza de la serpiente colisiona con la manzana
        if (manzana != null) {
            if (serpiente.get(0).colisiona(manzana)) {
                manzana = null;
                puntuacion++;
                addSpriteSerpiente();
                System.out.println(puntuacion);
            }
        }
    }

    /**
     * Este método se encargará de que la manzana siempre salga en una posición en
     * la que no se encuentre ninguna parte de la serpiente
     */
    private void crearManzanaFueraDeLaSerpiente() {
        crearManzana();
        // Se recorre la serpiente en busca de una coincidencia
        for (int i = 1; i < serpiente.size(); i++) {
            if (serpiente.get(i).colisiona(manzana)) {
                // si hay coincidencia se destruye la manzana para crear otra nueva
                manzana = null;
                break;
            }
        }
    }

    /**
     * Método encargado de generar una manzana nueva dentro de los límites del
     * tablero
     */
    private void crearManzana() {
        int posX;
        int posY;
        do {
            // saca una posición aleatoria dentro de los limites
            posX = new Random().nextInt((panelJuego.getWidth() - LADO_MANZANA));
            // comprueba si esa posición es múltiplo del tamaño de la manzana para que de un
            // efecto de rejilla y se vea todo mejor
            if (posX % LADO_MANZANA != 0) {
                // vuelve a empezar el bucle
                continue;
            }
            // misma lógica que la posX
            posY = new Random().nextInt((panelJuego.getHeight() - LADO_MANZANA) - ALTO_PANEL_SUPERIOR)
                    + ALTO_PANEL_SUPERIOR;
            if (posY % LADO_MANZANA != 0) {
                continue;
            }
            // en el caso de que la ejecución llegue aquí significa que las posiciones son
            // válidas y se puede salir del bucle
            break;
        } while (true);
        // crea una manzana nueva
        manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, posX, posY);
    }

    /**
     * Método que añade un {@link Sprite} más al ArrayList {@link #serpiente}
     * haciendo que esta creazca.
     * 
     * El método comprueba a que dirección se mueve el último eslabón de la cadena
     * de {@link Sprite} para añadir uno nuevo en la posición correcta
     */
    private void addSpriteSerpiente() {
        Sprite ultimoSprite = serpiente.get(serpiente.size() - 1);
        // El ultimo se mueve a la derecha
        if (ultimoSprite.getVelX() == LADO_SERPIENTE) {
            serpiente.add(new Sprite(RUTA_CUERPO, LADO_SERPIENTE, ultimoSprite.getPosX() - LADO_SERPIENTE,
                    ultimoSprite.getPosY()));
            // el ultimo de mueve a la izquierda
        } else if (ultimoSprite.getVelX() == -LADO_SERPIENTE) {
            serpiente.add(new Sprite(RUTA_CUERPO, LADO_SERPIENTE, ultimoSprite.getPosX() + LADO_SERPIENTE,
                    ultimoSprite.getPosY()));
            // el ultimo se mueve abajo
        } else if (ultimoSprite.getVelY() == LADO_SERPIENTE) {
            serpiente.add(new Sprite(RUTA_CUERPO, LADO_SERPIENTE, ultimoSprite.getPosX(),
                    ultimoSprite.getPosY() - LADO_SERPIENTE));
            // el ultimo se mueve arriba
        } else {
            serpiente.add(new Sprite(RUTA_CUERPO, LADO_SERPIENTE, ultimoSprite.getPosX(),
                    ultimoSprite.getPosY() + LADO_SERPIENTE));
        }

    }

    /**
     * Dependiendo de la dirección a la que se quiera mover se cambiará su velocidad
     * de movimiento
     */
    private void calcularMovimientoCabeza() {

        switch (direccion) {
            // Arriba
            case "UP":
                serpiente.get(0).setVelX(0);
                serpiente.get(0).setVelY(-LADO_SERPIENTE);
                serpiente.get(0).cambiarImagen(RUTA_CABEZA_UP);
                break;
            // Abajo
            case "DOWN":
                serpiente.get(0).setVelX(0);
                serpiente.get(0).setVelY(LADO_SERPIENTE);
                serpiente.get(0).cambiarImagen(RUTA_CABEZA_DOWN);
                break;
            // Derecha
            case "RIGHT":
                serpiente.get(0).setVelX(LADO_SERPIENTE);
                serpiente.get(0).setVelY(0);
                serpiente.get(0).cambiarImagen(RUTA_CABEZA_RIGHT);
                break;
            // Izquierda
            case "LEFT":
                serpiente.get(0).setVelX(-LADO_SERPIENTE);
                serpiente.get(0).setVelY(0);
                serpiente.get(0).cambiarImagen(RUTA_CABEZA_LEFT);
                break;
        }
    }

    /**
     * Método para pasar a una pantalla recibida por parametro
     * 
     * @param Pantalla a la que se quiere ir
     */
    private void pasarDePantalla(Pantalla siguientePantalla) {
        panelJuego.cambiarPantalla(siguientePantalla);
    }

    @Override
    public void escucharRaton(MouseEvent e) {

    }

    @Override
    public void moverRaton(MouseEvent e) {

    }

    @Override
    public void redimensionarPantalla(ComponentEvent e) {
        fondoRedimensionado = fondo.getScaledInstance(panelJuego.getWidth(), panelJuego.getHeight(),
                Image.SCALE_SMOOTH);
    }

    /**
     * Controla la tecla pulsada por el usuario para cabiar la dirección de
     * movimiento
     */
    @Override
    public void teclaPulsada(KeyEvent e) {

        if (!haTecleado) {
            // Derecha
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (direccion != "LEFT") {
                    direccion = "RIGHT";
                    haTecleado = true;
                }
                // Izquierda
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (direccion != "RIGHT") {
                    direccion = "LEFT";
                    haTecleado = true;

                }
                // Arriba
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (direccion != "DOWN") {
                    direccion = "UP";
                    haTecleado = true;

                }
                // Abajo
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (direccion != "UP") {
                    direccion = "DOWN";
                    haTecleado = true;

                }
            }
        }

    }

    /**
     * Devuelve la puntución obtenida en la partida
     */
    public static int getPuntuacion() {
        return puntuacion;
    }

}
