package com.elmika.tsp.adapter.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Exports RouteCoordinatesView to JSON for use by visualization adapters.
 */
public final class RouteCoordinatesJsonExporter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RouteCoordinatesJsonExporter() {
    }

    /**
     * Serializes the view to a JSON string.
     *
     * @param view the route coordinates view
     * @return JSON string, e.g. {"coordinates":[{"x":0.0,"y":0.0},...]}
     * @throws IllegalArgumentException if serialization fails
     */
    public static String toJson(RouteCoordinatesView view) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null.");
        }
        try {
            return MAPPER.writeValueAsString(view);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize route coordinates: " + e.getMessage(), e);
        }
    }
}
