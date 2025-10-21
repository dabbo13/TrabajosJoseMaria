package listadodirectorios;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Clase que permite realizar un volcado binario (en formato hexadecimal) de un fichero.
 */
public class VolcadoBinario {
    static int TAM_FILA = 32;
    static int MAX_BYTES = 2048;
    InputStream is = null;
    PrintStream salida = null;

    /**
     * Constructor de la clase.
     * @param is El InputStream desde el que se leerán los datos.
     * @param salida El PrintStream donde se escribirá el volcado.
     */
    public VolcadoBinario(InputStream is, PrintStream salida) {
        this.is = is;
        this.salida = salida;
    }

    /**
     * Realiza el volcado de los bytes del fichero.
     * @throws IOException Si ocurre un error de E/S.
     */
    public void volcar() throws IOException {
        byte buffer[] = new byte[TAM_FILA];
        int bytesLeidos;
        int offset = 0;

        do {
            bytesLeidos = is.read(buffer);
            if (bytesLeidos == -1) break;

            // Imprime en el PrintStream indicado
            salida.format("[%5d] ", offset);

            for (int i = 0; i < bytesLeidos; i++) {
                salida.format("%02x ", buffer[i]); // añadimos espacio para legibilidad
            }

            salida.println();
            offset += bytesLeidos;
        } while (bytesLeidos == TAM_FILA && offset < MAX_BYTES);
    }

    /**
     * Método principal (main) para la ejecución del programa.
     * Realiza el volcado de un fichero hacia otro fichero.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java accesosecuencial.VolcadoBinario <fichero_entrada> <fichero_salida>");
            return;
        }

        String nomFichEntrada = args[0];
        String nomFichSalida = args[1];

        try (
                FileInputStream fis = new FileInputStream(nomFichEntrada);
                PrintStream ps = new PrintStream(new FileOutputStream(nomFichSalida))
        ) {
            VolcadoBinario vb = new VolcadoBinario(fis, ps);
            vb.volcar();
            System.out.println("Volcado realizado correctamente en: " + nomFichSalida);
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: no existe fichero " + nomFichEntrada);
        }
        catch (IOException e) {
            System.err.println("ERROR de E/S: " + e.getMessage());
        }
    }
}
