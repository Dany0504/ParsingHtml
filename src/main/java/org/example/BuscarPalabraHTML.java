package org.example;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;

public class BuscarPalabraHTML extends HTMLEditorKit.ParserCallback {

    private String palabraClave;
    private int posicionActual;
    private boolean Body;
    private PrintWriter archivoLog;

    public BuscarPalabraHTML(String palabra, String nombreLog) throws Exception {
        this.palabraClave = palabra.toLowerCase();
        this.posicionActual = 0;
        this.Body = false;
        this.archivoLog = new PrintWriter(new FileWriter(nombreLog, true));
    }

    @Override
    public void handleText(char[] texto, int pos) {
        if (Body) {
            String textoEnMinusculas = new String(texto).toLowerCase();
            int index = textoEnMinusculas.indexOf(palabraClave);
            while (index >= 0) {
                int posicionPalabra = posicionActual + index;
                System.out.printf("La palabra '%s' está en la posición: %d%n", palabraClave, posicionPalabra);
                archivoLog.printf("La palabra '%s' está en la posición: %d%n", palabraClave, posicionPalabra);
                index = textoEnMinusculas.indexOf(palabraClave, index + 1);
            }
            posicionActual += textoEnMinusculas.length();
        }
    }

    @Override
    public void handleStartTag(HTML.Tag etiqueta, MutableAttributeSet atributos, int pos) {
        if (etiqueta == HTML.Tag.BODY) {
            Body = true;
        }
    }

    @Override
    public void handleEndTag(HTML.Tag etiqueta, int pos) {
        if (etiqueta == HTML.Tag.BODY) {
            Body = false;
        }
    }

    public void cerrarArchivoLog() {
        archivoLog.close();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java BuscarPalabraHTML <archivoHTML> <palabra>");
            return;
        }

        String archivoHTML = args[0];
        String palabra = args[1];
        String nombreLog = "file-" + palabra + ".log";

        try {
            Reader lectorArchivo = new FileReader(archivoHTML);
            BuscarPalabraHTML buscador = new BuscarPalabraHTML(palabra, nombreLog);
            HTMLEditorKit.Parser parser = new ParserDelegator();
            parser.parse(lectorArchivo, buscador, true);
            buscador.cerrarArchivoLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

