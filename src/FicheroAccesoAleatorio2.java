// Almacenamiento de registros de longitud fija en fichero acceso aleatorio

import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FicheroAccesoAleatorio2 {

    private File f;
    private List<Pair<String, Integer>> campos;
    private long longReg;
    private long numReg = 0;

    /**
     * Constructor para inicializar la clase y el archivo de acceso aleatorio.
     * @param nomFich El nombre del archivo a utilizar.
     * @param campos  Una lista de pares (nombre del campo, longitud del campo)
     * @throws IOException Si ocurre un error al acceder al archivo.
     */
    FicheroAccesoAleatorio2(String nomFich, List<Pair<String, Integer>> campos)
            throws IOException {
        this.campos = campos;
        this.f = new File(nomFich);
        longReg = 0;
        // Calcula la longitud total de un registro sumando las longitudes de todos los campos.
        for (Pair<String, Integer> campo : campos) {
            this.longReg += campo.getValue();
        }
        // Si el archivo ya existe, calcula el número de registros que contiene.
        if (f.exists()) {
            this.numReg = f.length() / this.longReg;
        }
    }

    /**
     * Devuelve el número de registros en el archivo.
     * @return El número de registros.
     */
    public long getNumReg() {
        return numReg;
    }

    /**
     * Inserta un nuevo registro al final del archivo.
     * @param reg Un mapa con los datos del registro (nombre del campo, valor).
     * @throws IOException Si ocurre un error de E/S.
     */
    public void insertar(Map<String, String> reg) throws IOException {
        insertar(reg, this.numReg++);
    }

    /**
     * Inserta un registro en una posición específica del archivo.
     * @param reg Un mapa con los datos del registro (nombre del campo, valor).
     * @param pos La posición (índice) donde se debe insertar el registro.
     * @throws IOException Si ocurre un error de E/S.
     */
    public void insertar(Map<String, String> reg, long pos) throws IOException {

        try (RandomAccessFile faa = new RandomAccessFile(f, "rws")) {
            faa.seek(pos * this.longReg);

            for (Pair<String, Integer> campo : this.campos) {
                String nomCampo = campo.getKey();
                Integer longCampo = campo.getValue();
                String valorCampo = reg.get(nomCampo);

                if (valorCampo == null) {
                    valorCampo = "";
                }

                String valorCampoForm = String.format("%1$-" + longCampo + "s", valorCampo);
                faa.write(valorCampoForm.getBytes("UTF-8"), 0, longCampo);
            }
        }
    }

    /**
     * Devuelve el valor de un campo de un registro dado su número de posición y el nombre del campo.
     * @param pos       Posición del registro (empezando en 0)
     * @param nomCampo  Nombre del campo cuyo valor se desea obtener
     * @return          Valor del campo como cadena de texto
     * @throws IOException Si ocurre un error de lectura o el campo no existe
     */
    public String obtenValorCampo(long pos, String nomCampo) throws IOException {

        if (pos >= numReg) {
            throw new IOException("La posición " + pos + " está fuera del rango de registros existentes (" + numReg + ")");
        }

        long offsetRegistro = pos * this.longReg;

        try (RandomAccessFile faa = new RandomAccessFile(f, "r")) {

            long desplazamientoCampo = 0;
            Integer longCampo = null;

            for (Pair<String, Integer> campo : this.campos) {
                if (campo.getKey().equalsIgnoreCase(nomCampo)) {
                    longCampo = campo.getValue();
                    break;
                } else {
                    desplazamientoCampo += campo.getValue();
                }
            }

            if (longCampo == null) {
                throw new IOException("El campo '" + nomCampo + "' no existe en la definición del registro.");
            }
            faa.seek(offsetRegistro + desplazamientoCampo);

            byte[] buffer = new byte[longCampo];
            faa.readFully(buffer);
            return new String(buffer, StandardCharsets.UTF_8).trim();
        }
    }

    /**
     * Devuelve todos los campos de un registro como un mapa <nombreCampo, valor>.
     * @param pos Posición del registro (empezando en 0)
     * @return Mapa con los pares campo=valor del registro.
     * @throws IOException Si ocurre un error de lectura.
     */
    public Map<String, String> obtenRegistro(long pos) throws IOException {

        if (pos >= numReg) {
            throw new IOException("La posición " + pos + " está fuera del rango de registros existentes (" + numReg + ")");
        }

        Map<String, String> registro = new HashMap<>();

        try (RandomAccessFile faa = new RandomAccessFile(f, "r")) {
            faa.seek(pos * this.longReg);

            for (Pair<String, Integer> campo : this.campos) {
                String nombre = campo.getKey();
                int longitud = campo.getValue();

                byte[] buffer = new byte[longitud];
                faa.readFully(buffer);
                String valor = new String(buffer, StandardCharsets.UTF_8).trim();
                registro.put(nombre, valor);
            }
        }

        return registro;
    }

    public static void main(String[] args) {

        // Define la estructura de los campos para los registros.
        List<Pair<String, Integer>> campos = new ArrayList<>();
        campos.add(new Pair<>("DNI", 9));
        campos.add(new Pair<>("NOMBRE", 32));
        campos.add(new Pair<>("CP", 5));

        try {
            FicheroAccesoAleatorio2 faa = new FicheroAccesoAleatorio2("fic_acceso_aleat.dat", campos);

            Map<String, String> reg = new HashMap<>();

            // --- Insertamos 3 registros normales ---
            reg.put("DNI", "56789012B");
            reg.put("NOMBRE", "SAMPER");
            reg.put("CP", "29730");
            faa.insertar(reg);

            reg.clear();
            reg.put("DNI", "89012345E");
            reg.put("NOMBRE", "ROJAS");
            faa.insertar(reg);

            reg.clear();
            reg.put("DNI", "23456789D");
            reg.put("NOMBRE", "DORCE");
            reg.put("CP", "13700");
            faa.insertar(reg);

            System.out.println("Número de registros actuales: " + faa.getNumReg());

            // --- Intentamos insertar un registro en posición 10 (más allá del final) ---
            reg.clear();
            reg.put("DNI", "99999999Z");
            reg.put("NOMBRE", "FUERA_DE_RANGO");
            reg.put("CP", "00000");

            faa.insertar(reg, 10);
            System.out.println("Se ha insertado un registro en la posición 10.");
            System.out.println("Tamaño final del fichero: " + new File("fic_acceso_aleat.dat").length() + " bytes");

            // --- Prueba de lectura ---
            System.out.println("\n=== PRUEBA DE LECTURA DE CAMPOS ===");
            System.out.println("Nombre del registro 0: " + faa.obtenValorCampo(0, "NOMBRE"));
            System.out.println("DNI del registro 1: " + faa.obtenValorCampo(1, "DNI"));
            System.out.println("CP del registro 2: " + faa.obtenValorCampo(2, "CP"));

            System.out.println("\n=== PRUEBA DE LECTURA DE REGISTRO COMPLETO ===");
            Map<String, String> regCompleto = faa.obtenRegistro(0);
            for (Map.Entry<String, String> e : regCompleto.entrySet()) {
                System.out.println(e.getKey() + ": " + e.getValue());
            }

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
