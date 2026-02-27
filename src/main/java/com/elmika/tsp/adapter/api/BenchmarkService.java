package com.elmika.tsp.adapter.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.elmika.tsp.adapter.api.dto.BenchmarkEntryDto;
import com.elmika.tsp.adapter.api.dto.BenchmarkResultDto;

/**
 * Pure ranking service for benchmark results. No computation, no external dependencies.
 * Sorts provided solutions by distance and computes gap percentages.
 */
public class BenchmarkService {

    public List<BenchmarkResultDto> rank(List<BenchmarkEntryDto> entries) {
        if (entries == null || entries.isEmpty()) {
            return new ArrayList<>();
        }
        List<BenchmarkEntryDto> sorted = new ArrayList<>(entries);
        sorted.sort(Comparator.comparingDouble(BenchmarkEntryDto::getDistance));

        double best = sorted.get(0).getDistance();
        List<BenchmarkResultDto> results = new ArrayList<>(sorted.size());
        for (int i = 0; i < sorted.size(); i++) {
            BenchmarkEntryDto entry = sorted.get(i);
            double gap = best > 0 ? (entry.getDistance() - best) / best * 100.0 : 0.0;
            results.add(new BenchmarkResultDto(
                i + 1,
                entry.getLabel(),
                entry.getDistance(),
                Math.round(gap * 10.0) / 10.0,
                entry.getDurationMs()
            ));
        }
        return results;
    }
}
