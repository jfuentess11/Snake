import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Sprite
 * @author Javier Fuentes
 */
public class Sprite {
    private BufferedImage buffer;
    private Color color;
    private int ancho;
    private int alto;
    private int posX;
    private int posY;
    private int velX;
    private int velY;
    private Image imageRedimensionada;

    public Sprite() {
	}

    public Sprite(Color color, int lado, int posX, int posY){
        this.color = color;
        this.ancho = lado;
        this.alto = lado;
        this.posX = posX;
        this.posY = posY;
        this.velX = 0;
        this.velY = 0;

        inicializarBuffer();
    }

    public Sprite(String ruta, int lado, int posX, int posY){
        this.color = null;
        this.ancho = lado;
        this.alto = lado;
        this.posX = posX;
        this.posY = posY;
        this.velX = 0;
        this.velY = 0;

        inicializarBufferImagen(ruta);     
    }

	private void inicializarBuffer() {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffer.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, ancho, alto);
        g.dispose();
    }

    private void inicializarBufferImagen(String ruta) {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageSprite = null;
        try {
            imageSprite = ImageIO.read(new File(ruta));
            Graphics g = buffer.getGraphics();
            imageRedimensionada = imageSprite.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            g.drawImage(imageRedimensionada, 0, 0, null);
            g.dispose();
        } catch (IOException e) {
            
        }  
    }

    public void estampar(Graphics g) {

        if(this.color != null){
            g.drawImage(buffer, posX, posY, null);
        }else{
            g.drawImage(imageRedimensionada, posX, posY, null);
        }
    
    }

    public void mover(){
        posX += velX;
        posY += velY;
    }

    public boolean colisiona(Sprite s2) {

        boolean colisionEjeX = colisionEnEje(posX, s2.getPosX(), ancho/2, s2.getAncho()/2);

        if(!colisionEjeX){
            return false;
        }

        boolean colisionEjeY = colisionEnEje(posY, s2.getPosY(), alto/2, s2.getAlto()/2);

        return colisionEjeX && colisionEjeY;
    }

    private boolean colisionEnEje(int pos_s1, int pos_s2, int tamano_s1, int tamano_s2) {
        boolean colisionEje;

        if (pos_s1 < pos_s2) {
            colisionEje = comprobarColision(pos_s1, pos_s2, tamano_s1);
        } else {
            colisionEje = comprobarColision(pos_s2, pos_s1, tamano_s2);
        }
        return colisionEje;
    }

    private boolean comprobarColision(int pos_sCercano, int pos_sLejano, int tamano) {
        
        if ((pos_sCercano + tamano) >= pos_sLejano) {
            return true;
        }
        return false;
    }

    // GETTERS && SETTERS

    public BufferedImage getBuffer() {
        return this.buffer;
    }

    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getAncho() {
        return this.ancho;
    }

    public void setAncho(int acho) {
        this.ancho = acho;
    }

    public int getAlto() {
        return this.alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getVelX() {
        return this.velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return this.velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

}
