package com.elmika.tsp.solvers;

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
                solver = new RandomSolver(problem, 10);
                sol = solver.findSolution();
                break;
            case "random100":
            solver = new RandomSolver(problem, 100);
            sol = solver.findSolution();
            break;
            default:
            solver = new RandomSolver(problem, 20);
            sol = solver.findSolution();
        }

        return sol;
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
