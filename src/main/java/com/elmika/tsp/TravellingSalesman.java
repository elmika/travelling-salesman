package com.elmika.tsp;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        double[][] problem;
        String option = "bigger";
        switch(option){
            case "trivial": problem = ProblemFactory.createSimplestProblem(); break;
            case "simple": problem = ProblemFactory.createSimpleProblem(); break;
            case "bigger": problem = ProblemFactory.createBiggerProblem(); break;
            default: problem = ProblemFactory.createSimplestProblem(); break;
        }

        SimpleSolver solver = new SimpleSolver(problem);
        int[] solution = solver.findSolution();

        displaySolution(solution, solver);
    }

    private static void displaySolution(int[] sol, SimpleSolver solver){

        String solString = ""+sol[0];
        for(int i = 1; i < sol.length; i++) {
            solString+="->"+sol[i];
        }

        System.out.println("Solution is:"+solString);
        System.out.println("Distance is:"+solver.getTotalDistance(sol));
    }

}
