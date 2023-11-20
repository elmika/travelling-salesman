package com.elmika.tsp;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        // JSONParsing.test();
        ProblemConfiguration config = JSONParsing.getConfig();
        double[][] problem = ProblemFactory.createProblem(config.getProblem());        

        SimpleSolver solver = new SimpleSolver(problem);
        Integer[] solution = solver.findSolution(config.getResolutionStrategy());

        displaySolution(solution, solver);
    }

    private static void displaySolution(Integer[] sol, SimpleSolver solver){

        String solString = ""+sol[0];
        for(int i = 1; i < sol.length; i++) {
            solString+="->"+sol[i];
        }

        System.out.println("Solution is:"+solString);
        System.out.println("Distance is:"+solver.getTotalDistance(sol));
    }

}
