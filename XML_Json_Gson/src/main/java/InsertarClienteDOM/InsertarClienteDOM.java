package InsertarClienteDOM;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class InsertarClienteDOM {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java InsertarClienteDOM <ficheroXML>");
            return;
        }

        String nombreFichero = args[0];

        try {
            // Configurar parser DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(nombreFichero));

            // Validar que el documento tiene un único elemento raíz <clientes>
            Element raiz = doc.getDocumentElement();
            if (!"clientes".equals(raiz.getNodeName())) {
                System.err.println("Error: El documento no tiene un único elemento raíz <clientes>.");
                return;
            }

            // Crear nuevo cliente con atributo DNI
            Element nuevoCliente = doc.createElement("cliente");
            nuevoCliente.setAttribute("DNI", "11111111A");

            // Crear subelementos
            Element apellidos = doc.createElement("apellidos");
            apellidos.setTextContent("GARCIA FERNANDEZ");

            Element cp = doc.createElement("CP");
            cp.setTextContent("12345");

            // Añadir los subelementos al cliente
            nuevoCliente.appendChild(apellidos);
            nuevoCliente.appendChild(cp);

            // Insertar el nuevo cliente al principio
            Node primerCliente = raiz.getFirstChild();
            raiz.insertBefore(nuevoCliente, primerCliente);

            // Configurar Transformer para salida XML formateada
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Escribir XML resultante por System.out
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

        } catch (SAXException e) {
            System.err.println("Error: El fichero no es un XML bien formado.");
        } catch (IOException e) {
            System.err.println("Error: No se puede leer el fichero " + nombreFichero);
        } catch (ParserConfigurationException e) {
            System.err.println("Error en la configuración del parser: " + e.getMessage());
        } catch (TransformerException e) {
            System.err.println("Error al transformar el documento: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

