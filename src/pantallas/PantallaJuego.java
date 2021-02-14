package pantallas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import principal.PanelJuego;
import principal.Pantalla;
import principal.Sprite;

/**
 * Pantalla de Juego
 * 
 * @author Javier Fuentes
 */
public class PantallaJuego implements Pantalla {

    // Referencia al Panel de Juego
    private PanelJuego panelJuego;

    // CONSTANTES SERPIENTE
    private int LADO_SERPIENTE = 15;
    private Color COLOR_SERPIENTE = Color.BLUE;

    // CONSTANTES MANZANA
    private int LADO_MANZANA = 15;
    private String RUTA_MANZANA = "Imagenes/manzana.png";

    private Sprite manzana;
    private ArrayList<Sprite> serpiente;
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;
    private String direccion;
    private int puntuacion;
    private boolean finJuego = false;

    // Constructor
    public PantallaJuego(PanelJuego panelJuego) {
        this.panelJuego = panelJuego;
    }

    @Override
    public void inicializarPantalla() {
        serpiente = new ArrayList<Sprite>();
        serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, LADO_SERPIENTE, LADO_SERPIENTE));

        //manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, LADO_MANZANA * 2, LADO_MANZANA * 2);
        manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, 360, 340);

        direccion = "DOWN";
        puntuacion = 0;
        try {
            fondo = ImageIO.read(new File("Imagenes/fondo.jpg"));
        } catch (Exception e) {

        }

    }

    @Override
    public void pintarPantalla(Graphics g) {
        rellenarFondo(g);

        if (manzana != null) {
            manzana.estampar(g);
        }

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
        if (!finJuego) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            panelJuego.requestFocus();

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

            if (serpiente.size() > 4) {
                for (int i = 0; i < serpiente.size() - 1; i++) {
                    for (int j = i + 4; j < serpiente.size(); j++) {

                        if (serpiente.get(i).colisiona(serpiente.get(j))) {
                            finJuego = true;
                        }
                    }
                }
            }

            if (serpiente.get(0).getPosX() > panelJuego.getWidth()) {
                finJuego = true;
            } else if (serpiente.get(0).getPosX() < 0) {
                finJuego = true;
            } else if (serpiente.get(0).getPosY() < 0) {
                finJuego = true;
            } else if (serpiente.get(0).getPosY() > panelJuego.getHeight()) {
                finJuego = true;
            }

            if (manzana != null) {
                if (serpiente.get(0).colisiona(manzana)) {
                    manzana = null;
                    puntuacion++;
                    addSpriteSerpiente();
                    System.out.println(puntuacion);
                }
            }

            if (manzana == null) {
                crearManzana();
            }

            if (finJuego) {
                System.out.println("END");
            }
        }
    }

    private void crearManzana() {
        boolean haySerpiente = false;
        int posX;
        int posY;
        do {
            do {
                posX = new Random().nextInt((panelJuego.getWidth() - LADO_MANZANA));
                if (posX % LADO_MANZANA != 0) {
                    continue;
                }
                posY = new Random().nextInt((panelJuego.getHeight() - LADO_MANZANA));
                if (posY % LADO_MANZANA != 0) {
                    continue;
                }
                break;
            } while (true);

            for (Sprite sprite : serpiente) {

                int posXSerpiente = sprite.getPosX();
                int posYSerpiente = sprite.getPosY();

                if (posX == posXSerpiente && posY == posYSerpiente) {
                    haySerpiente = true;
                    break;
                }
            }
        } while (haySerpiente);

        manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, posX, posY);
    }

    private void addSpriteSerpiente() {
        Sprite ultimoSprite = serpiente.get(serpiente.size() - 1);
        // El ultimo se mueve a la derecha
        if (ultimoSprite.getVelX() == LADO_SERPIENTE) {
            serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, ultimoSprite.getPosX() - LADO_SERPIENTE,
                    ultimoSprite.getPosY()));
            // el ultimo de mueve a la izquierda
        } else if (ultimoSprite.getVelX() == -LADO_SERPIENTE) {
            serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, ultimoSprite.getPosX() + LADO_SERPIENTE,
                    ultimoSprite.getPosY()));
            // el ultimo se mueve abajo
        } else if (ultimoSprite.getVelY() == LADO_SERPIENTE) {
            serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, ultimoSprite.getPosX(),
                    ultimoSprite.getPosY() - LADO_SERPIENTE));
            // el ultimo se mueve arriba
        } else {
            serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, ultimoSprite.getPosX(),
                    ultimoSprite.getPosY() + LADO_SERPIENTE));
        }

    }

    private void calcularMovimientoCabeza() {

        switch (direccion) {
            case "UP":
                serpiente.get(0).setVelX(0);
                serpiente.get(0).setVelY(-LADO_SERPIENTE);
                break;

            case "DOWN":
                serpiente.get(0).setVelX(0);
                serpiente.get(0).setVelY(LADO_SERPIENTE);
                break;

            case "RIGHT":
                serpiente.get(0).setVelX(LADO_SERPIENTE);
                serpiente.get(0).setVelY(0);
                break;

            case "LEFT":
                serpiente.get(0).setVelX(-LADO_SERPIENTE);
                serpiente.get(0).setVelY(0);
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

    @Override
    public void teclaPulsada(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (direccion != "LEFT") {
                direccion = "RIGHT";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (direccion != "RIGHT") {
                direccion = "LEFT";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (direccion != "DOWN") {
                direccion = "UP";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (direccion != "UP") {
                direccion = "DOWN";
            }
        }
    }

}