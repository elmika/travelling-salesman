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
# This uses the shaded JAR manifest to locate the main class.
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

## Visualizing a route

This repository includes a small static viewer to visualize a solved TSP route.

- **Expected JSON format**: the viewer expects the JSON produced from a `RouteCoordinatesView`
  via `RouteCoordinatesJsonExporter.toJson(view)`, for example:

  ```json
  {
    "coordinates": [
      { "x": 0.0, "y": 0.0 },
      { "x": 3.0, "y": 4.0 }
    ]
  }
  ```

  The route is interpreted as an ordered list of cities and is visually closed by
  drawing a final segment from the last city back to the first.

- **Using the viewer**:
  1. Place `index.html` and your `route.json` (or another JSON filename) in the project root.
  2. Start a simple HTTP server from the project root, for example:

     ```bash
     python -m http.server 8000
     ```

  3. Open `http://localhost:8000/index.html` in your browser.
  4. Enter the JSON filename if it is not `route.json`, then click **Load route** to see the path.


### Smoke testing the packaged application

For a quick smoke test that the shaded JAR builds and starts correctly, you can use the helper script:

```bash
# Locally, if you have Java and Maven:
./smoke-test.sh

# Or inside Docker:
docker run tsp-solver ./smoke-test.sh
```