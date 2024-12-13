package com.elmika.tsp.solvers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.elmika.tsp.problems.Problem;

public class SimpleSolver {

    private Problem problem;

    public SimpleSolver(Problem problem) {
        this.problem = problem;
    }

    protected Problem getProblem() {
        return this.problem;
    }

    public Integer[] findSolution(String type) {

        Integer[] sol;
        Solver solver;
        switch (type) {
            case "brute-force":
                solver = new BruteForceSolver(problem);
                sol = solver.findSolution();
                break;
            case "random":
                solver = new RandomSolver(problem);
                sol = solver.findSolution();
                break;
            case "random10":
                System.out.println("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
                break;
            case "random100":
                System.out.println("Comparing 100 random solutions to find the best route.");
                sol = findBestRandomSolution(100);
                break;
            default:
                System.out.println("Comparing 10 random solutions to find the best route.");
                sol = findBestRandomSolution(10);
        }

        return sol;
    }

    private Integer[] findBestRandomSolution(Integer iterations) {

        Integer[] solution = this.findRandomSolution();
        double distance = this.getTotalDistance(solution);

        for (Integer i = 0; i < iterations; i++) {
            Integer[] newSolution = findRandomSolution();
            double newDistance = this.getTotalDistance(newSolution);
            if (newDistance < distance) {
                solution = newSolution;
                distance = newDistance;
            }
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
