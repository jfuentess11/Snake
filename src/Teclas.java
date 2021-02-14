import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Teclas
 * 
 * Clase encargada de la gestión de detección de teclas que pulse el usuario para mover la serpiente del panel de juego
 * @see PanelJuego
 * @author Javier Fuentes
 */
public class Teclas implements KeyListener {

    String teclaPulsada = "DOWN";

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (teclaPulsada != "LEFT") {
                teclaPulsada = "RIGHT";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (teclaPulsada != "RIGHT") {
                teclaPulsada = "LEFT";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (teclaPulsada != "DOWN") {
                teclaPulsada = "UP";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (teclaPulsada != "UP") {
                teclaPulsada = "DOWN";
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    public String getTeclaPulsada(){
        return teclaPulsada;
    }
}
