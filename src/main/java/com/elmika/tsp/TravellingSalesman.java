package com.elmika.tsp;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        double[][] problem = ProblemFactory.createSimpleProblem();
        SimpleSolver solver = new SimpleSolver(problem);
        int[] solution = solver.findSolution();

        displaySolution(solution, problem);
    }

    private static void displaySolution(int[] sol, double[][] pb){

        String solString = ""+sol[0];
        for(int i = 1; i < sol.length; i++) {
            solString+="->"+sol[i];
        }

        System.out.println("Solution is:"+solString);
        System.out.println("Distance is:"+SimpleSolver.getTotalDistance(sol, pb));
    }

}
