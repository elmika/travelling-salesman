package com.elmika.tsp.adapter.api.dto;

import java.util.List;

public class ProblemTypesResponse {

    private final List<String> tsplibTypes;

    public ProblemTypesResponse(List<String> tsplibTypes) {
        this.tsplibTypes = tsplibTypes;
    }

    public List<String> getTsplibTypes() {
        return tsplibTypes;
    }
}
