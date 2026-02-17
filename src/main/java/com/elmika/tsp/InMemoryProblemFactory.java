package com.elmika.tsp;

/**
 * Driven adapter for ProblemProvider. Creates problems from built-in types
 * (trivial, simple, bigger, euclidean, citiesN, fully-randomN, partially-randomN)
 * via ProblemFactory. Delegates to existing in-memory creation logic.
 */
public class InMemoryProblemFactory implements ProblemProvider {

    @Override
    public Problem create(String problemType) {
        return ProblemFactory.createProblem(problemType);
    }
}
