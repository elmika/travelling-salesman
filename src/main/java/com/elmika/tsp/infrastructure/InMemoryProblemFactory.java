package com.elmika.tsp.infrastructure;

import java.util.List;

import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.domain.problem.Problem;

/**
 * Driven adapter for ProblemProvider. Creates problems from built-in types
 * via ProblemFactory.
 */
public class InMemoryProblemFactory implements ProblemProvider {

    @Override
    public Problem create(String problemType) {
        return ProblemFactory.createProblem(problemType);
    }

    @Override
    public List<String> tsplibNames() {
        return TspLibParser.availableNames();
    }
}
