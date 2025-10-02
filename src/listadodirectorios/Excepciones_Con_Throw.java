package listadodirectorios;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Excepciones_Con_Throw {
    public File creaFicheroTempConCar(String prefNomFich, char car, int numRep) throws IOException {
        File f = File.createTempFile(prefNomFich, "");
        FileWriter fw = new FileWriter(f);
        for (int i = 0; i < numRep; i++) fw.write(car);
        fw.close();
        return f;
    }

    public static void main(String[] args) {
        try {
            File ft = new Excepciones_Con_Throw().creaFicheroTempConCar("AAAA", 'A', 20);
            System.out.println("Creado fichero: " + ft.getAbsolutePath());
            ft.delete();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}