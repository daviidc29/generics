package edu.eci.arsw.generics.io;

import edu.eci.arsw.generics.collections.SimpleLinkedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Lector de archivos de números reales
 * Reglas:
 *   -Admite separadores por coma y/o espacios
 *   -Ignora líneas en blanco
 *   -Ignora comentarios que inician con '#'
 */
public class DoubleFileReader {

    /**
     * Lee un archivo de texto y retorna una lista con los doubles encontrados.
     *
     * @param path ruta del archivo a leer
     * @return lista de valores @link Double
     * @throws IOException si ocurre un error de E/S
     * @throws NumberFormatException si algún token no puede parsearse como double
     */
    public List<Double> read(String path) throws IOException {
        SimpleLinkedList<Double> result = new SimpleLinkedList<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                // Remover comentario a partir de '#'
                int hash = line.indexOf('#');
                if (hash >= 0) {
                    line = line.substring(0, hash);
                }
                line = line.trim();
                if (line.isEmpty()) continue;

                // Separadores: comas y/o espacios
                String[] tokens = line.split("[,\\s]+");
                for (String token : tokens) {
                    if (token.isEmpty()) continue;
                    // Double.parseDouble usa '.' independientemente del Locale actual
                    double val = Double.parseDouble(token);
                    result.add(val);
                }
            }
        }
        return result;
    }
}
