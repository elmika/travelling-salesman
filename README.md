# Travelling Salesman Project solver

TSP is a classic algorithmic problem in the field of computer science and operations research. It asks for the shortest possible route that visits a set of locations exactly once and returns to the origin point.

In this project, we implement some problems and algorithms to resolve them.


## Architecture

The project follows **Hexagonal Architecture**. See [ARCHITECTURE.md](ARCHITECTURE.md) for the target architecture diagram and dependency rules.

## Project Structure

```
src/main/java/com/elmika/tsp/
├── domain/           Problem, Solution, EuclideanProblem, DistanceMatrixProblem
├── application/      ConfigLoader, ProblemProvider, TspSolver, SolveTspUseCase,
│                     SimpleSolver, PermutationsIterator, ProblemConfiguration
├── infrastructure/   JsonFileConfigLoader, InMemoryProblemFactory,
│                     JSONParsing, ProblemFactory
└── adapter/cli/      TspCli, TravellingSalesman (main)
```

## Running with Docker

You can also run this application using Docker to avoid installing Maven and Java directly on your machine.

### Building the Docker Image

To build the Docker image, run:

```bash
docker build -t tsp-solver .
```

### Running the application

Then, run the application:

```bash
docker run tsp-solver
```

Or if you are editing the code:

```bash
docker run -it -v $(pwd):/app -w /app tsp-solver /bin/bash
```
and then
```bash
mvn clean package
java -jar target/tsp-solver-0.1-SETUP.jar
```

### Executing the tests

The project has automated tests run by JUnit 5 via Maven.

```
docker build -t tsp-solver .
docker run tsp-solver mvn test
```