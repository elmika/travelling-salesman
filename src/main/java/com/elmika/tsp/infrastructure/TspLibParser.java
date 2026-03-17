package com.elmika.tsp.infrastructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.elmika.tsp.application.ProblemTypeParser;
import com.elmika.tsp.domain.problem.EuclideanProblem;
import com.elmika.tsp.domain.problem.Problem;

/**
 * Parses TSPLIB95 EUC_2D problem files from the classpath.
 *
 * <p>Files are expected to be located at {@code tsplib/<name>.tsp} on the classpath
 * (i.e. {@code src/main/resources/tsplib/}).
 *
 * <p>Supported format: standard TSPLIB95 text format with {@code NODE_COORD_SECTION}
 * containing lines of the form {@code id x y}. Both {@code KEY: value} and
 * {@code KEY : value} header styles are handled.
 *
 * <p>Only {@code EDGE_WEIGHT_TYPE: EUC_2D} is supported. Attempting to load a file
 * with any other weight type will throw {@link IllegalArgumentException}.
 *
 * <p>Sources: see {@code src/main/resources/tsplib/SOURCES.md}.
 */
public class TspLibParser {

    private TspLibParser() {
    }

    /**
     * Returns the names of all available TSPLIB problems as full type strings (e.g. {@code "tsplib-berlin52"}).
     * The list is sorted alphabetically.
     *
     * @throws IllegalStateException if the classpath resources cannot be scanned
     */
    public static List<String> availableNames() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:tsplib/*.tsp");
            List<String> names = new ArrayList<>(resources.length);
            for (Resource r : resources) {
                String filename = r.getFilename();
                if (filename != null && filename.endsWith(".tsp")) {
                    names.add(ProblemTypeParser.TSPLIB + filename.substring(0, filename.length() - 4));
                }
            }
            Collections.sort(names);
            return Collections.unmodifiableList(names);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to list TSPLIB resources", e);
        }
    }

    /**
     * Loads and parses the named TSPLIB problem file.
     *
     * @param name problem name without extension, e.g. {@code "berlin52"}
     * @return an {@link EuclideanProblem} with the problem's city coordinates
     * @throws IllegalArgumentException if the file is missing, malformed, or not EUC_2D
     */
    public static Problem load(String name) {
        String resourcePath = "tsplib/" + name + ".tsp";
        InputStream in = TspLibParser.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException(
                "Unknown TSPLIB problem '" + name + "'. " +
                "No file found at classpath resource: " + resourcePath);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return parse(name, reader);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read TSPLIB file for '" + name + "': " + e.getMessage(), e);
        }
    }

    private static Problem parse(String name, BufferedReader reader) throws IOException {
        int dimension = -1;
        boolean isEuc2d = false;
        boolean inCoordSection = false;
        double[][] coords = null;
        int coordsRead = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.equals("EOF")) {
                break;
            }

            if (inCoordSection) {
                if (line.equals("EOF") || line.equals("DEMAND_SECTION") || line.equals("DEPOT_SECTION")) {
                    break;
                }
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    coords[coordsRead][0] = x;
                    coords[coordsRead][1] = y;
                    coordsRead++;
                }
                continue;
            }

            if (line.startsWith("DIMENSION")) {
                dimension = Integer.parseInt(extractValue(line));
                coords = new double[dimension][2];
            } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                String wt = extractValue(line);
                if (!wt.equals("EUC_2D")) {
                    throw new IllegalArgumentException(
                        "TSPLIB problem '" + name + "' has EDGE_WEIGHT_TYPE=" + wt +
                        "; only EUC_2D is supported.");
                }
                isEuc2d = true;
            } else if (line.equals("NODE_COORD_SECTION")) {
                if (dimension < 1) {
                    throw new IllegalArgumentException(
                        "TSPLIB file for '" + name + "' reached NODE_COORD_SECTION before DIMENSION was set.");
                }
                if (!isEuc2d) {
                    throw new IllegalArgumentException(
                        "TSPLIB file for '" + name + "' reached NODE_COORD_SECTION but EDGE_WEIGHT_TYPE was not EUC_2D.");
                }
                inCoordSection = true;
            }
        }

        if (coords == null || coordsRead == 0) {
            throw new IllegalArgumentException(
                "TSPLIB file for '" + name + "' contains no coordinates.");
        }
        if (coordsRead != dimension) {
            throw new IllegalArgumentException(
                "TSPLIB file for '" + name + "': expected " + dimension +
                " coordinates but read " + coordsRead + ".");
        }

        return new EuclideanProblem(coords);
    }

    /** Extracts the value from a header line of the form {@code KEY: value} or {@code KEY : value}. */
    private static String extractValue(String line) {
        int colon = line.indexOf(':');
        return line.substring(colon + 1).trim();
    }
}
