package com.elmika.tsp.solvers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.elmika.tsp.problems.Problem;

public class RandomSolver implements Solver {

    private Problem problem;

    public RandomSolver(Problem problem) {
        this.problem = problem;
    }

    protected Problem getProblem() {
        return this.problem;
    }

    @Override 
    public Integer[] findSolution() {
        System.out.println("Finding one random solution.");
        return findRandomSolution();
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
