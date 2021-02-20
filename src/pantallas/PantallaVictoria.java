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
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import principal.PanelJuego;
import principal.Pantalla;
import principal.Record;

/**
 * Pantalla de Victoria
 * 
 * @author Javier Fuentes
 */
public class PantallaVictoria implements Pantalla {

    // Referencia al Panel de Juego
    private PanelJuego panelJuego;

    // Color del texto
    private Color texto;
    private Color inicio;
    private Color record;

    // Fuentes de la pantalla del inicio
    private Font fuenteGrande;
    private Font fuentePequenna;

    // Imagen de fondo redimensionada
    private BufferedImage fondo = null;
    private Image fondoRedimensionado;

    // Tiempo 
    private long tiempo;

    // Formato tiempo
    private DecimalFormat formatoTiempo;

    // Fichero de records
    private Record ficheroRecord;
    
    // COmprobar si ha habido record de tiempo
    private boolean esRecord;

    // Constructor
    public PantallaVictoria(PanelJuego panelJuego) {
        this.panelJuego = panelJuego;
        texto = Color.GREEN;
        inicio = Color.WHITE;
        record = Color.RED;
        formatoTiempo = new DecimalFormat("#");
        fuenteGrande = new Font("Arial", Font.BOLD, 30);
        fuentePequenna = new Font("Arial", Font.BOLD ,15);
        tiempo = PantallaJuego.getTiempo();
        ficheroRecord = new Record();
        esRecord = ficheroRecord.comprobarRecord(tiempo);
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
        // Texto de derrota
        g.setFont(fuenteGrande);
        g.setColor(texto);
        g.drawString("¡HAS GANADO!", 120, 100);
        // Texto indicando el tiempo obtenido
        g.setFont(fuentePequenna);
        g.drawString("Tu tiempo ha sido: ", 100, 135);
        g.setFont(fuenteGrande);
        g.drawString(tiempoFormateado() + " seg", 255, 135);
        // Testo de nuevo record
        if(esRecord){
            g.setFont(fuentePequenna);
            record = (record == Color.RED?Color.BLUE:Color.RED);
            g.setColor(record);
            g.drawString("Has conseguido un NUEVO RECORD", 100, 160);
        }
        // Texto para que indica que pulses para volver a jugar que irá cambiando de
        // color entre blanco y gris
        inicio = (inicio == Color.WHITE ? Color.GRAY : Color.WHITE);
        g.setFont(fuentePequenna);
        g.setColor(inicio);
        g.drawRect((120) - 10, (panelJuego.getHeight() - 150) - 30, 150, 50);
        g.drawString("VOLVER A JUGAR", 120, panelJuego.getHeight() - 150);
    }

    /**
     * Método que devuelve el tiempo formateado
     * @return tiempo formateado
     */
    private String tiempoFormateado() {
        String time = formatoTiempo.format(tiempo/1e9);
        if(Integer.parseInt(time) < 10){
            time = "000"+time;
        }else if(Integer.parseInt(time) < 100){
            time = "00"+time;
        }else if(Integer.parseInt(time) < 1000){
            time = "0"+time;
        }
        return time;
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
    }
    
}
