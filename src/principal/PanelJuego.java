package principal;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import pantallas.PantallaInicio;

/**
 * Panel de Juego
 * 
 * @author Javier Fuentes
 */
public class PanelJuego extends JPanel
        implements Runnable, MouseListener, MouseMotionListener, ComponentListener, KeyListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Pantalla por la que estamos
    private Pantalla pantallaActual;

    /**
     * Constructor
     */
    public PanelJuego() {
        pantallaActual = new PantallaInicio(this);
        pantallaActual.inicializarPantalla();

        // Añadir Hilo y Eventos
        new Thread(this).start();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addComponentListener(this);
        this.addKeyListener(this);
    }

    /**
     * Método que cambiará la pantala actual que se encuatra en ejecución por la que
     * recibe por parámetro
     * 
     * @param nuevaPantalla a la que se quiere cambiar
     */
    public void cambiarPantalla(Pantalla nuevaPantalla) {
        nuevaPantalla.inicializarPantalla();
        pantallaActual = nuevaPantalla;

    }

    // Método que se llama automáticamente para pintar el componente.
    @Override
    public void paintComponent(Graphics g) {
        pantallaActual.pintarPantalla(g);
    }

    @Override
    public void run() {
        while (true) {
            pantallaActual.ejecutarFrame();
            repaint();
            Toolkit.getDefaultToolkit().sync();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pantallaActual.escucharRaton(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        pantallaActual.moverRaton(e);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        pantallaActual.redimensionarPantalla(e);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pantallaActual.teclaPulsada(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
