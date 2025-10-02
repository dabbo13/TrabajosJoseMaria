package listadodirectorios;

import java.io.*;

public class Actividad_4_Ficheros {
    public static void main(String[] args) {
        String ficheroEntrada = "entrada_utf8.txt";
        String ficheroISO = "salida_iso8859_1.txt";
        String ficheroUTF16 = "salida_utf16.txt";

        try (
                // Leemos el fichero de entrada como UTF-8
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(ficheroEntrada), "UTF-8"))
        ) {
            try (BufferedWriter bwISO = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(ficheroISO), "ISO-8859-1"));
                 BufferedWriter bwUTF16 = new BufferedWriter(
                         new OutputStreamWriter(new FileOutputStream(ficheroUTF16), "UTF-16"))
            ) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    // Escribimos la línea en ambos ficheros
                    bwISO.write(linea);
                    bwISO.newLine();

                    bwUTF16.write(linea);
                    bwUTF16.newLine();
                }
                System.out.println("Conversión realizada con éxito.");
            }

        } catch (IOException e) {
            System.err.println("Error en la conversión: " + e.getMessage());
        }
    }
}

