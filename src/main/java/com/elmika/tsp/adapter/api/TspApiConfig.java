package com.elmika.tsp.adapter.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.elmika.tsp.application.ProblemProvider;
import com.elmika.tsp.infrastructure.InMemoryProblemFactory;

@Configuration
public class TspApiConfig {

    @Bean
    public ProblemProvider problemProvider() {
        return new InMemoryProblemFactory();
    }

    @Bean
    public ResolutionService resolutionService() {
        return new ResolutionService();
    }

    @Bean
    public ImprovementService improvementService() {
        return new ImprovementService();
    }

    @Bean
    public BenchmarkService benchmarkService() {
        return new BenchmarkService();
    }
}
