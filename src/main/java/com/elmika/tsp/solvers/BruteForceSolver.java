package com.elmika.tsp.solvers;

import com.elmika.tsp.PermutationsIterator;
import com.elmika.tsp.problems.Problem;

public class BruteForceSolver implements Solver {

    private Problem problem;
    private PermutationsIterator iterator;

    public BruteForceSolver(Problem problem) {
        this.problem = problem;
    }

    protected Problem getProblem() {
        return this.problem;
    }

    private PermutationsIterator getPermutationsIterator() {
        if (this.iterator == null) {
            int problemSize = this.problem.getSize();
            this.iterator = new PermutationsIterator(problemSize);
        }
        return this.iterator;
    }

    @Override 
    public Integer[] findSolution() {
        System.out.println("Using Brute Force algorithm to find the best route.");
        return findBestSolution();
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
