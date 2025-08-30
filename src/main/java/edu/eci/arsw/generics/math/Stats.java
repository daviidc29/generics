package edu.eci.arsw.generics.math;

import java.util.List;

/**
 * Utilidades estadísticas: media y desviación estándar muestral (n-1)
 */
public final class Stats {

    private Stats() { }

    /**
     * Calcula la media aritmética de una lista de @link Double
     *
     * @param data lista de datos no nula y no vacía
     * @return media aritmética
     * @throws IllegalArgumentException si @code data es nula o está vacía
     */
    public static double mean(List<Double> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("La lista de datos no puede ser nula ni vacía.");
        }
        double sum = 0.0;
        for (Double d : data) {
            if (d == null) {
                throw new IllegalArgumentException("Los datos no pueden contener valores nulos.");
            }
            sum += d;
        }
        return sum / data.size();
        // double es suficiente para los datasets del enunciado
    }

    /**
     * Calcula la desviación estándar muestral denominador n-1
     *
     * @param data lista de datos n &gt;= 2
     * @return desviación estándar muestral.
     * @throws IllegalArgumentException si @code data es nula, tiene elementos nulos
     *                                  o su tamaño es &lt; 2
     */
    public static double sampleStdDev(List<Double> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("La lista de datos no puede ser nula ni vacía.");
        }
        int n = data.size();
        if (n < 2) {
            throw new IllegalArgumentException("Se requieren al menos 2 datos para desviación estándar muestral.");
        }
        double mean = mean(data);

        double sumSq = 0.0;
        for (Double d : data) {
            if (d == null) {
                throw new IllegalArgumentException("Los datos no pueden contener valores nulos.");
            }
            double diff = d - mean;
            sumSq += diff * diff;
        }
        return Math.sqrt(sumSq / (n - 1));
    }
}
