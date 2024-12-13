package com.elmika.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.elmika.tsp.problems.EuclideanProblem;
import com.elmika.tsp.problems.Problem;
import com.elmika.tsp.problems.ProblemFactory;
import com.elmika.tsp.solvers.SimpleEuclideanSolver;
import com.elmika.tsp.solvers.SimpleSolver;

public class TravellingSalesman {

    public static void main(String[] args) {
        System.out.println("Traveling Salesman Problem Solver");

        // JSONParsing.test();
        ProblemConfiguration config = JSONParsing.getConfig();
        Problem problem = ProblemFactory.createProblem(config.getProblem());        

        SimpleSolver solver;
        if(problem instanceof EuclideanProblem) {
            solver = new SimpleEuclideanSolver(problem);
        } else {
            solver = new SimpleSolver(problem);
        }

        Integer[] solution = solver.findSolution(config.getResolutionStrategy());

        displaySolution(solution, solver);
    }

    private static void displaySolution(Integer[] sol, SimpleSolver solver){

        String solString = ""+sol[0];
        for(int i = 1; i < sol.length; i++) {
            solString+="->"+sol[i];
        }

        System.out.println("Solution is:"+solString);
        if(solver instanceof SimpleEuclideanSolver) {
            SimpleEuclideanSolver esolver = (SimpleEuclideanSolver)solver;
            displayEuclideanSolution(sol, esolver);
        }
        System.out.println("Distance is:"+solver.getTotalDistance(sol));
    }

    private static void displayEuclideanSolution(Integer[] sol, SimpleEuclideanSolver solver){

        double[][] coordinates = solver.getSolutionCoordinates(sol);

        // Convert array to list of Coordinate objects
        // Convert array to list of maps (x, y pairs)
        List<Map<String, Double>> coordinateList = new ArrayList<>();
        for (double[] coord : coordinates) {
            coordinateList.add(Map.of("x", coord[0], "y", coord[1]));
        }

        // Convert list to JSON string
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(coordinateList);
        System.out.println("Solution coordinates are:"+json);

    }

}
