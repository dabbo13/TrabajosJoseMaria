package listadodirectorios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Actividad_3_Ficheros {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduce el nombre del fichero: ");
        String nombreFichero = sc.nextLine();

        System.out.print("Introduce el texto a buscar: ");
        String textoBuscado = sc.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                int indice = linea.indexOf(textoBuscado);
                while (indice != -1) {
                    System.out.printf("Encontrado en línea %d, columna %d%n", numeroLinea, indice + 1);

                    indice = linea.indexOf(textoBuscado, indice + 1);
                }
                numeroLinea++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }
}
