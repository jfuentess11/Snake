package pantallas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import principal.PanelJuego;
import principal.Pantalla;

/**
 * Pantalla de Derrota
 * 
 * @author Javier Fuentes
 */
public class PantallaVictoria implements Pantalla {

    // Referencia al Panel de Juego
    private PanelJuego panelJuego;

    // Color del texto
    private Color texto;
    private Color inicio;

    // Fuentes de la pantalla del inicio
    private Font fuenteGrande;
    private Font fuentePequenna;

    // Imagen de fondo redimensionada
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;

    private int puntuacion;

    // Constructor
    public PantallaVictoria(PanelJuego panelJuego) {
        this.panelJuego = panelJuego;
        texto = Color.GREEN;
        inicio = Color.WHITE;
        fuenteGrande = new Font("Arial", Font.BOLD, 30);
        fuentePequenna = new Font("Arial", Font.BOLD ,15);
        puntuacion = PantallaJuego.getPuntuacion();
    }

    @Override
    public void inicializarPantalla() {
        // Cargamos el fondo de pantalla
        try {
            fondo = ImageIO.read(new File("Imagenes/fondoFinal.jpg"));
        } catch (Exception e) {
        }
        // Fuerza redimensionar la pantalla para actualizar el fondo
        redimensionarPantalla(null);
    }

    @Override
    public void pintarPantalla(Graphics g) {
        // Pinta el fondo de pantalla
        rellenarFondo(g);
        // Texto de derrota con la fuente y el color querido
        g.setFont(fuenteGrande);
        g.setColor(texto);
        g.drawString("¡HAS GANADO!", 120, 100);
        g.setFont(fuentePequenna);
        g.drawString("Tu puntuación ha sido: ", 155, 120);
        g.setFont(fuenteGrande);
        g.drawString(""+puntuacion, 210, 150);
        // Texto para que indica que pulses para volver a jugar que irá cambiando de
        // color entre blanco y gris
        inicio = (inicio == Color.WHITE ? Color.GRAY : Color.WHITE);
        g.setFont(fuentePequenna);
        g.setColor(inicio);
        g.drawRect((120) - 10, (panelJuego.getHeight() - 150) - 30, 150, 50);
        g.drawString("VOLVER A JUGAR", 120, panelJuego.getHeight() - 150);
    }

    @Override
    public void rellenarFondo(Graphics g) {
        g.drawImage(fondoRedimensionado, 0, 0, null);
    }

    @Override
    public void ejecutarFrame() {
        try {
            Thread.sleep(333);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void escucharRaton(MouseEvent e) {
        // Camba a la pantalla de Juego en el panel de juego
        panelJuego.cambiarPantalla(new PantallaJuego(panelJuego));

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
        // TODO Auto-generated method stub

    }
    
}
