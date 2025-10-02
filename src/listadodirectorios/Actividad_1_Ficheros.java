
package listadodirectorios;
import java.text.SimpleDateFormat;
import java.io.File;

public class Actividad_1_Ficheros {
    public static void main(String[] args) {
        //Inicializar variables
        //Representa el directorio actual
        String ruta = ".";
        SimpleDateFormat sdf = new SimpleDateFormat();
        if(args.length >= 1) {
            ruta = args[0];
        }
        File fich = new File(ruta);
        if(!fich.exists()){
            System.out.println("No existe fichero o directorio en " + ruta);
        } else {
            if(fich.isFile()){
                System.out.println("Es un fichero" + ruta);
                System.out.println("Con tamaño: " + fich.length());
            } else{
                System.out.println(ruta + "es un directorio. Contenidos: ");
                File[] ficheros = fich.listFiles();
                for(File f: ficheros){
                    String texto = f.isDirectory() ? "(/)" + f.getName() : f.isFile() ? "(_)" + f.getName() : ")";
                    System.out.println("(" + texto + ")" + fich.length() + " bytes y modificado: " + sdf.format(fich.lastModified()));
                    System.out.print("Permisos: ");
                    if(fich.canExecute() ==  true){
                        System.out.print("X");
                    }else{
                        System.out.print("_");
                    }
                    if (fich.canRead() == true){
                        System.out.print("R");
                    }else{
                        System.out.print("_");
                    }
                    if (fich.canWrite() == true){
                        System.out.print("L\n");
                    }else{
                        System.out.print("_\n");
                    }
                }
            }

        }
    }
}
