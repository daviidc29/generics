package edu.eci.arsw.generics;

import edu.eci.arsw.generics.io.DoubleFileReader;
import edu.eci.arsw.generics.math.Stats;

import java.util.List;
import java.util.Locale;

/**
 * Punto de entrada de la aplicación CLI
 * Uso:
 * java -jar target/generics-1.0.0.jar --input &lt;ruta-al-archivo&gt;
 */
public final class App {

    private App() { }

    /**
     * Main de la aplicación.
     * @param args argumentos de línea de comandos. Se espera @code --input <ruta>
     */
    public static void main(String[] args) {
        String inputPath = null;
        for (int i = 0; i < args.length; i++) {
            if ("--input".equals(args[i]) && i + 1 < args.length) {
                inputPath = args[i + 1];
                break;
            }
        }

        if (inputPath == null) {
            System.err.println("Uso: java -jar target/generics-1.0.0.jar --input <ruta-al-archivo>");
            System.exit(1);
        }

        try {
            DoubleFileReader reader = new DoubleFileReader();
            List<Double> data = reader.read(inputPath);

            double mean = Stats.mean(data);
            double std = Stats.sampleStdDev(data);

            System.out.printf(Locale.ROOT, "Media: %.2f%n", mean);
            System.out.printf(Locale.ROOT, "Desviación estándar muestral: %.2f%n", std);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            System.exit(2);
        }
    }
}
