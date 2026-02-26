package com.elmika.tsp.application.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

/**
 * Initial solver that builds a tour by greedy edge insertion.
 *
 * <p>All possible edges are sorted by distance. Edges are added shortest-first,
 * skipping any that would give a city degree > 2 or create a premature cycle
 * (i.e. a cycle before all cities are connected). The last edge is allowed to
 * close the cycle, completing the tour.
 *
 * <p>Typically produces better starting tours than nearest-neighbor, because it
 * considers the globally shortest edges rather than greedy local steps.
 *
 * <p>Complexity: O(n² log n) for sorting, O(n²) for selection.
 */
public class GreedyEdgeSolver implements SolverStrategy {

    private static final Logger log = LoggerFactory.getLogger(GreedyEdgeSolver.class);

    @Override
    public Solution solve(Problem problem) {
        log.info("Using Greedy Edge Insertion to build a tour.");
        int n = problem.getSize();

        List<int[]> edges = sortedEdges(problem, n);

        int[] degree   = new int[n + 1];
        int[] parent   = initParent(n);
        List<List<Integer>> neighbors = initNeighbors(n);

        int edgesAdded = 0;
        for (int[] edge : edges) {
            if (edgesAdded == n) break;
            int i = edge[0], j = edge[1];
            if (degree[i] >= 2 || degree[j] >= 2) continue;
            if (edgesAdded < n - 1 && find(parent, i) == find(parent, j)) continue;
            neighbors.get(i).add(j);
            neighbors.get(j).add(i);
            degree[i]++;
            degree[j]++;
            union(parent, i, j);
            edgesAdded++;
        }

        Integer[] route = buildRoute(neighbors, n);
        return new Solution(route, totalDistance(problem, route));
    }

    /**
     * Returns all city-pair edges sorted by distance ascending.
     * Distances are precomputed into a parallel array; indices are then sorted
     * so that each getDistance() call happens exactly once.
     */
    private static List<int[]> sortedEdges(Problem problem, int n) {
        int m = n * (n - 1) / 2;
        int[][] pairs = new int[m][2];
        double[] dist = new double[m];
        int k = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                pairs[k][0] = i;
                pairs[k][1] = j;
                dist[k] = problem.getDistance(i, j);
                k++;
            }
        }
        Integer[] indices = new Integer[m];
        for (int i = 0; i < m; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> Double.compare(dist[a], dist[b]));
        List<int[]> sorted = new ArrayList<>(m);
        for (int idx : indices) sorted.add(pairs[idx]);
        return sorted;
    }

    /** Reconstructs the ordered tour by traversing the adjacency list from city 1. */
    private static Integer[] buildRoute(List<List<Integer>> neighbors, int n) {
        Integer[] route = new Integer[n];
        boolean[] visited = new boolean[n + 1];
        route[0] = 1;
        visited[1] = true;
        for (int step = 1; step < n; step++) {
            int current = route[step - 1];
            for (int neighbor : neighbors.get(current)) {
                if (!visited[neighbor]) {
                    route[step] = neighbor;
                    visited[neighbor] = true;
                    break;
                }
            }
        }
        return route;
    }

    private static int[] initParent(int n) {
        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) parent[i] = i;
        return parent;
    }

    private static List<List<Integer>> initNeighbors(int n) {
        List<List<Integer>> neighbors = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) neighbors.add(new ArrayList<>());
        return neighbors;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path compression
            x = parent[x];
        }
        return x;
    }

    private static void union(int[] parent, int a, int b) {
        parent[find(parent, a)] = find(parent, b);
    }

    private static double totalDistance(Problem problem, Integer[] route) {
        if (route.length == 0) {
            return 0;
        }
        double total = problem.getDistance(route[route.length - 1], route[0]);
        for (int i = 1; i < route.length; i++) {
            total += problem.getDistance(route[i - 1], route[i]);
        }
        return total;
    }
}
