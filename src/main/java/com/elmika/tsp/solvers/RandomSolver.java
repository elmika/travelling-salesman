package com.elmika.tsp.solvers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.elmika.tsp.problems.Problem;

public class RandomSolver implements Solver {

    private Problem problem;
    private Integer iterations;

    public RandomSolver(Problem problem) {
        this.problem = problem;
        this.iterations = 1;
    }

    public RandomSolver(Problem problem, Integer iterations) {
        this.problem = problem;
        this.iterations = iterations;
    }

    protected Problem getProblem() {
        return this.problem;
    }

    @Override 
    public Integer[] findSolution() {
        if (this.iterations == 1) {
            System.out.println("Finding one random solution.");
            return findRandomSolution();
        } else {
            System.out.println("Comparing "+iterations+" random solutions to find the best route.");
            return findBestRandomSolution();
        }
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

    private Integer[] findBestRandomSolution() {
        
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
