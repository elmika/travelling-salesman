package com.elmika.tsp.adapter.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TspControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── GET /api/problems ─────────────────────────────────────────

    @Test
    void getProblems_knownEuclideanType_returnsPoints() throws Exception {
        mockMvc.perform(get("/api/problems/cities10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points", hasSize(10)));
    }

    @Test
    void getProblems_circleType_returnsPoints() throws Exception {
        mockMvc.perform(get("/api/problems/circle-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points", hasSize(10)));
    }

    @Test
    void getProblems_clusterType_returnsPoints() throws Exception {
        mockMvc.perform(get("/api/problems/cluster-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points", hasSize(20)));
    }

    @Test
    void getProblems_tsplibType_returnsPoints() throws Exception {
        mockMvc.perform(get("/api/problems/tsplib-berlin52"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points", hasSize(52)));
    }

    @Test
    void getProblems_tsplibUnknownName_returns400() throws Exception {
        mockMvc.perform(get("/api/problems/tsplib-doesnotexist"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getProblems_distanceMatrixType_returns400() throws Exception {
        // 'trivial' produces a DistanceMatrixProblem, not a Euclidean problem
        mockMvc.perform(get("/api/problems/trivial"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // ── POST /api/solve ───────────────────────────────────────────

    @Test
    void solve_nearestNeighbor_returnsRoute() throws Exception {
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":3,\"y\":4},{\"x\":6,\"y\":0},{\"x\":3,\"y\":0}],"
                    + "\"strategy\":\"nearest-neighbor\"}";
        mockMvc.perform(post("/api/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route", hasSize(4)))
                .andExpect(jsonPath("$.totalDistance").isNumber())
                .andExpect(jsonPath("$.strategy").value("nearest-neighbor"))
                .andExpect(jsonPath("$.durationMs").isNumber());
    }

    @Test
    void solve_bruteForce_tooLarge_returns400() throws Exception {
        // 13 cities exceeds brute-force sync limit of 12
        StringBuilder sb = new StringBuilder("{\"points\":[");
        for (int i = 0; i < 13; i++) {
            if (i > 0) sb.append(",");
            sb.append("{\"x\":").append(i * 7).append(",\"y\":").append(i * 3).append("}");
        }
        sb.append("],\"strategy\":\"brute-force\"}");
        mockMvc.perform(post("/api/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sb.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void solve_bruteForce_exactLimit_succeeds() throws Exception {
        // 4 cities — well within the brute-force limit
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":3,\"y\":4},{\"x\":6,\"y\":0},{\"x\":3,\"y\":0}],"
                    + "\"strategy\":\"brute-force\"}";
        mockMvc.perform(post("/api/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route", hasSize(4)));
    }

    @Test
    void solve_unknownStrategy_returns400() throws Exception {
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1}],\"strategy\":\"nonexistent\"}";
        mockMvc.perform(post("/api/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void solve_missingPoints_returns400() throws Exception {
        String body = "{\"strategy\":\"nearest-neighbor\"}";
        mockMvc.perform(post("/api/solve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── POST /api/improve ─────────────────────────────────────────

    @Test
    void improve_twoOpt_returnsImprovedRoute() throws Exception {
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":3,\"y\":4},{\"x\":6,\"y\":0},{\"x\":3,\"y\":0}],"
                    + "\"solution\":{\"route\":[1,2,3,4],\"totalDistance\":20.0},"
                    + "\"strategy\":\"2opt\"}";
        mockMvc.perform(post("/api/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route", hasSize(4)))
                .andExpect(jsonPath("$.totalDistance").isNumber())
                .andExpect(jsonPath("$.strategy").value("2opt"))
                .andExpect(jsonPath("$.originalDistance").value(20.0))
                .andExpect(jsonPath("$.improvementPercent").isNumber())
                .andExpect(jsonPath("$.durationMs").isNumber());
    }

    @Test
    void improve_unknownStrategy_returns400() throws Exception {
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1}],"
                    + "\"solution\":{\"route\":[1,2],\"totalDistance\":5.0},"
                    + "\"strategy\":\"nonexistent\"}";
        mockMvc.perform(post("/api/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void improve_missingSolution_returns400() throws Exception {
        String body = "{\"points\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1}],\"strategy\":\"2opt\"}";
        mockMvc.perform(post("/api/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── POST /api/benchmark ───────────────────────────────────────

    @Test
    void benchmark_twoEntries_returnsRankedResults() throws Exception {
        String body = "{\"solutions\":["
                    + "{\"label\":\"solver-a\",\"distance\":15.0,\"durationMs\":2},"
                    + "{\"label\":\"solver-b\",\"distance\":12.0,\"durationMs\":45}"
                    + "]}";
        mockMvc.perform(post("/api/benchmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].rank").value(1))
                .andExpect(jsonPath("$.results[0].label").value("solver-b"))
                .andExpect(jsonPath("$.results[0].gapPercent").value(0.0))
                .andExpect(jsonPath("$.results[1].rank").value(2))
                .andExpect(jsonPath("$.results[1].label").value("solver-a"))
                .andExpect(jsonPath("$.results[1].gapPercent").value(25.0));
    }

    @Test
    void benchmark_emptySolutions_returns400() throws Exception {
        String body = "{\"solutions\":[]}";
        mockMvc.perform(post("/api/benchmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
