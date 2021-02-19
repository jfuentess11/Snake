package principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Record {
    private final File FICHERO = new File("./record.txt");
    private BufferedReader br;
    private BufferedWriter bw;
    private long record = Long.MAX_VALUE;

    public Record() {
    }

    /**
     * Método que compara el nuevo tiempo obtenido con el record anterior
     * 
     * Si se supera el record anterior lo escribe en el fichero para guardarlo
     */
    public boolean comprobarRecord(long recordAux) {
        boolean esRecord = false;
        try {
            br = new BufferedReader(new FileReader(FICHERO));
            String linea;
            if ((linea = br.readLine()) != null) {
                record = Long.parseLong(linea);

            }
            if (record > recordAux) {
                record = recordAux;
                esRecord = true;
                escribirNuevoRecordEnFichero();
            }

            br.close();
        } catch (IOException e) {
        }
        return esRecord;
    }

    private void escribirNuevoRecordEnFichero() {
        try {
            bw = new BufferedWriter(new FileWriter(FICHERO));
            bw.write(String.valueOf(record));
            bw.close();
        } catch (IOException e) {
        }
    }
}
