package com.elmika.tsp;

// import java.util.stream.Collectors;
// import java.util.stream.Stream;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        double[][] problem = ProblemFactory.createSimpleProblem();
        SimpleSolver solver = new SimpleSolver(problem);
        int[] solution = solver.findSolution(problem);

        displaySolution(solution, problem);
    }

    private static void displaySolution(int[] sol, double[][] pb){

        System.out.println("Solution is:"+sol[0]+
                            "->"+sol[1]+
                            "->"+sol[2]+
                            "->"+sol[3]+
                            "->"+sol[4]
        );

        System.out.println("Distance is:"+SimpleSolver.getTotalDistance(sol, pb));
    }

}
