package Practica;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear productos
            Producto p1 = new Producto("1", "Manzana", 0.5, 100);
            Producto p2 = new Producto("2", "Leche", 1.2, 50);
            Producto p3 = new Producto("3", "Pan", 0.8, 30);

            // Añadir productos a la lista
            List<Producto> listaProductos = new ArrayList<>();
            listaProductos.add(p1);
            listaProductos.add(p2);
            listaProductos.add(p3);

            // Crear inventario
            Inventario inventario = new Inventario("Tienda Comiditas", listaProductos);

            // Crear ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print

            // Serializar a JSON y guardar en archivo
            mapper.writeValue(new File("inventario.json"), inventario);
            System.out.println("Inventario serializado correctamente en inventario.json");

            // Deserializar desde el archivo
            Inventario inventarioRecuperado = mapper.readValue(new File("inventario.json"), Inventario.class);
            System.out.println("\nInventario deserializado:");

            // Verificación: imprimir detalles
            System.out.println("Nombre de la tienda: " + inventarioRecuperado.getNombreTienda());
            for (Producto prod : inventarioRecuperado.getProductos()) {
                System.out.println(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

