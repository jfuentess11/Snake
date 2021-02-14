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
import java.util.Random;

import javax.imageio.ImageIO;

import principal.PanelJuego;
import principal.Pantalla;

/**
 * Pantalla de Inicio
 * 
 * @author Javier Fuentes
 */
public class PantallaInicio implements Pantalla {
    // Referencia al Panel de Juego
    private PanelJuego panelJuego;

    // Color del texto
    private Color texto;

    // Fuente de la pantalla del inicio
    private Font fuenteInicio;

    // Imagen de fondo redimensionada
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;

    // Constructor
    public PantallaInicio(PanelJuego panelJuego) {
        this.panelJuego = panelJuego;
        texto = Color.WHITE;
        fuenteInicio = new Font("Arial", Font.BOLD, 30);
    }

    @Override
    public void inicializarPantalla() {
        // Cargamos el fondo de pantalla
        try {
            fondo = ImageIO.read(new File("Imagenes/fondoSerpiente.jpg"));
        } catch (Exception e) {
        }

    }

    @Override
    public void pintarPantalla(Graphics g) {
        // Forzamos redimensionar la pantalla para actualizar el fondo de pantalla
        redimensionarPantalla(null);
        // Pinta el fondo de pantalla
        rellenarFondo(g);
        // Escribe un título con la fuente y el color querido
        g.setFont(fuenteInicio);
        g.setColor(texto);
        g.drawString("SNAKE CLÁSICO", panelJuego.getWidth() / 2 - 120,
                panelJuego.getHeight() / 4);

        // Pinta un cuadrado y un texto de empezar a jugar con colores aleatorios en
        // cada refresco
        // Los colores que saldrá serán colores claros para verse mejor sobre el fondo
        // oscuro
        g.setColor(new Color(colorOscuro(), colorOscuro() , colorOscuro()));
        g.drawRect(80 - 10, (panelJuego.getHeight() - 100) - 30, 165, 80);
        g.drawString("Comenzar", 80, panelJuego.getHeight() - 100);
        g.drawString("a Jugar", 100, panelJuego.getHeight() - 60);

    }

    private float colorOscuro() {
        float n = new Random().nextInt(6);
        return n/10;
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
        // Cambia a la pantalla de juego en el panel de juego
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
