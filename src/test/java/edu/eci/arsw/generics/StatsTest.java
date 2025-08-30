package edu.eci.arsw.generics;

import edu.eci.arsw.generics.io.DoubleFileReader;
import edu.eci.arsw.generics.math.Stats;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsTest {

    private String resourcePath(String name) {
        try {
            var url = Thread.currentThread().getContextClassLoader().getResource(name);
            if (url == null) throw new IllegalStateException("Recurso no encontrado: " + name);
            return Paths.get(url.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Ruta inválida para recurso: " + name, e);
        }
    }

    @Test
    void column1_shouldMatchExpectedMeanAndSampleStdDev() throws Exception {
        DoubleFileReader reader = new DoubleFileReader();
        List<Double> data = reader.read(resourcePath("table1.txt"));

        double mean = Stats.mean(data);
        double std = Stats.sampleStdDev(data);

        assertEquals(550.60, mean, 0.01, "mean col1");
        assertEquals(572.03, std, 0.01, "sample stdDev col1");
    }

    @Test
    void column2_shouldMatchExpectedMeanAndSampleStdDev() throws Exception {
        DoubleFileReader reader = new DoubleFileReader();
        List<Double> data = reader.read(resourcePath("table2.txt"));

        double mean = Stats.mean(data);
        double std = Stats.sampleStdDev(data);

        assertEquals(60.32, mean, 0.01, "mean col2");
        assertEquals(62.26, std, 0.01, "sample stdDev col2");
    }

    @Test
    void column3_shouldMatchExpectedMeanAndSampleStdDev() throws Exception {
        DoubleFileReader reader = new DoubleFileReader();
        List<Double> data = reader.read(resourcePath("table3.txt"));

        double mean = Stats.mean(data);
        double std = Stats.sampleStdDev(data);

        assertEquals(638.90, mean, 0.01, "mean col3");
        assertEquals(625.63, std, 0.01, "sample stdDev col3");
    }
}
