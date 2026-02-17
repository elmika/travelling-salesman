package com.elmika.tsp.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.domain.Problem;
import com.elmika.tsp.domain.Solution;

public class SimpleSolver {

    private static final Logger log = LoggerFactory.getLogger(SimpleSolver.class);

    private final Problem problem;
    private PermutationsIterator iterator;

    public SimpleSolver(Problem problem) {
        this.problem = problem;
    }

    private PermutationsIterator getPermutationsIterator() {
        if (this.iterator == null) {
            this.iterator = new PermutationsIterator(this.problem.getSize());
        }
        return this.iterator;
    }

    public Solution findSolution(String type) {
        Integer[] sol;
        switch (type) {
            case "brute-force":
                log.info("Using Brute Force algorithm to find the best route.");
                sol = findBestSolution();
                break;
            case "random":
                log.info("Finding one random solution.");
                sol = findRandomSolution();
                break;
            case "random10":
                log.info("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
                break;
            case "random100":
                log.info("Comparing 100 random solutions to find the best route.");
                sol = findBestRandomSolution(100);
                break;
            default:
                log.info("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
        }
        double distance = getTotalDistance(sol);
        return new Solution(sol, distance);
    }

    private Integer[] findBestRandomSolution(Integer iterations) {
        Integer[] solution = findRandomSolution();
        double distance = getTotalDistance(solution);
        for (Integer i = 0; i < iterations; i++) {
            Integer[] newSolution = findRandomSolution();
            double newDistance = getTotalDistance(newSolution);
            if (newDistance < distance) {
                solution = newSolution;
                distance = newDistance;
            }
        }
        return solution;
    }

    public Integer[] findBestSolution() {
        iterator = getPermutationsIterator();
        Integer[] solution = iterator.next();
        if (!iterator.hasNext()) {
            return solution;
        }
        double distance = getTotalDistance(solution);
        Integer[] newSolution = iterator.next();
        while (iterator.hasNext()) {
            double newDistance = getTotalDistance(newSolution);
            if (newDistance < distance) {
                solution = newSolution;
                distance = newDistance;
            }
            newSolution = iterator.next();
        }
        return solution;
    }

    private Integer[] findRandomSolution() {
        Integer[] solution = new Integer[this.problem.getSize()];
        for (int i = 0; i < this.problem.getSize(); i++) {
            solution[i] = i + 1;
        }
        List<Integer> intList = Arrays.asList(solution);
        Collections.shuffle(intList);
        Integer[] intSolution = new Integer[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            intSolution[i] = intList.get(i);
        }
        return intSolution;
    }

    public double getTotalDistance(Integer[] sol) {
        if (sol.length == 0) {
            return 0;
        }
        double totalDistance = this.problem.getDistance(sol[sol.length - 1], sol[0]);
        for (int i = 1; i < sol.length; i++) {
            totalDistance += this.problem.getDistance(sol[i - 1], sol[i]);
        }
        return totalDistance;
    }
}
