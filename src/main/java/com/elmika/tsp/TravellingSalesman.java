package com.elmika.tsp;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        // JSONParsing.test();
        ProblemConfiguration config = JSONParsing.getConfig();
        Problem problem = ProblemFactory.createProblem(config.getProblem());

        SimpleSolver solver = new SimpleSolver(problem);
        Solution solution = solver.findSolution(config.getResolutionStrategy());

        displaySolution(solution);
    }

    private static void displaySolution(Solution solution){
        Integer[] sol = solution.getRoute();

        StringBuilder solStringBuilder = new StringBuilder();
        if (sol.length > 0) {
            solStringBuilder.append(sol[0]);
            for (int i = 1; i < sol.length; i++) {
                solStringBuilder.append("->").append(sol[i]);
            }
        }

        System.out.println("Solution is:" + solStringBuilder);
        System.out.println("Distance is:" + solution.getTotalDistance());
    }

}
