import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Javier Fuentes
 */
public class PanelJuego extends JPanel implements Runnable, ComponentListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int LADO_SERPIENTE = 15;
    private Color COLOR_SERPIENTE = Color.BLUE;

    private int LADO_MANZANA = 15;
    private String RUTA_MANZANA = "Imagenes/manzana.png";

    private Sprite manzana;
    private ArrayList<Sprite> serpiente;
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;

    private Teclas teclas;

    private boolean finJuego = false;
    
    /**
     * Constructor
     */
    public PanelJuego() {
        
        teclas = new Teclas();
        this.addComponentListener(this);
        this.addKeyListener(teclas);
        
        serpiente = new ArrayList<Sprite>();
        serpiente.add(new Sprite(COLOR_SERPIENTE, LADO_SERPIENTE, LADO_SERPIENTE, LADO_SERPIENTE));

        manzana = new Sprite(RUTA_MANZANA, LADO_MANZANA, LADO_MANZANA*6, LADO_MANZANA*5);

        try {
            fondo = ImageIO.read(new File("Imagenes/fondo.jpg"));
        } catch (Exception e) {

        }
        new Thread(this).start();
    }

    // Método que se llama automáticamente para pintar el componente.
    @Override
    public void paintComponent(Graphics g) {
        rellenarFondo(g);

        if (manzana != null) {
            manzana.estampar(g);
        }

        for (Sprite sprite : serpiente) {
            sprite.estampar(g);
        }

    }

    /**
     * Método para rellenar el fondo del componente.
     * 
     * @param g
     */
    private void rellenarFondo(Graphics g) {
        g.drawImage(fondoRedimensionado, 0, 0, null);
    }

    @Override
    public void run() {
        while (true) {
            if(!finJuego){
                this.requestFocus();
                try {
                    Thread.sleep(150);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.requestFocus();
                
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
    
                if(serpiente.size()>4){
                    for (int i = 0; i < serpiente.size()-1; i++) {
                        for (int j = i+4; j < serpiente.size(); j++) {
                            
                            if(serpiente.get(i).colisiona(serpiente.get(j))){  
                                finJuego = true;
                            }
                        }
                    }
                }
                
                
                if(serpiente.get(0).getPosX() > getWidth()){
                    finJuego = true;
                }else if(serpiente.get(0).getPosX() < 0){
                    finJuego = true;
                }else if(serpiente.get(0).getPosY() < 0){
                    finJuego = true;
                }else if(serpiente.get(0).getPosY() > getHeight()){
                    finJuego = true;
                }
                
                if (manzana != null) {
                    if (serpiente.get(0).colisiona(manzana)) {
                        manzana = null;
                        addSpriteSerpiente();
                    }
                }
    
                if (manzana == null) {
                    crearManzana();
                }

                if(finJuego){
                    System.out.println("END");
                }
                repaint();
                Toolkit.getDefaultToolkit().sync();
            }
           
        }
    }

    private void crearManzana() {
        boolean haySerpiente = false;
        int posX;
        int posY;
        do {
            do {
                posX = new Random().nextInt((getWidth() - LADO_MANZANA));
                posY = new Random().nextInt((getHeight() - LADO_MANZANA));
            } while (posX % LADO_MANZANA != 0 || posY % LADO_MANZANA != 0);

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

        switch (teclas.getTeclaPulsada()) {
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

    @Override
    public void componentResized(ComponentEvent e) {
        fondoRedimensionado = fondo.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }
}
