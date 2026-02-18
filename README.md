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
docker run --rm \
  -v "$(pwd)/problemConfiguration.json":/app/problemConfiguration.json:ro \
  tsp-solver
# This uses the shaded JAR manifest to locate the main class and the config file from your working directory.
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

```bash
docker build -t tsp-solver .
docker run tsp-solver mvn test
```

## Visualizing a route

This repository includes a small static viewer (`index.html`) to visualize a solved TSP route.

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

- **Generating `route.json` from the CLI (Docker)**:

  When you run the CLI with an Euclidean problem type, the application will export the last
  solved route to `output/route.json` (overwriting any existing file). This file is ignored
  by Git. To do this entirely inside Docker and keep the output available for the viewer:

  1. Build the solver image (as above):

     ```bash
     docker build -t tsp-solver .
     ```

  2. Run the solver container with a named volume for the output and your local configuration:

     ```bash
     docker volume create tsp-output

     docker run --rm \
       -v "$(pwd)/problemConfiguration.json":/app/problemConfiguration.json:ro \
       -v tsp-output:/app/output \
       tsp-solver
     ```

     The CLI will write `/app/output/route.json` into the `tsp-output` volume.

- **Serving the viewer via Docker only (nginx)**:

  To visualize the generated route without any local HTTP server:

  ```bash
  docker run --rm -p 8000:80 \
    -v tsp-output:/usr/share/nginx/html/output:ro \
    -v "$(pwd)/index.html":/usr/share/nginx/html/index.html:ro \
    nginx:alpine
  ```

  Then open `http://localhost:8000/index.html` in your browser and, in the viewer, use
  `output/route.json` as the JSON filename before clicking **Load route**. This setup keeps
  both the solver and the viewer inside Docker while sharing the generated route via the
  `tsp-output` Docker volume.


### Smoke testing the packaged application

For a quick smoke test that the shaded JAR builds and starts correctly, you can use the helper script:

```bash
# Locally, if you have Java and Maven:
./smoke-test.sh

# Or inside Docker (reusing your local problemConfiguration.json):
docker run --rm \
  -v "$(pwd)/problemConfiguration.json":/app/problemConfiguration.json:ro \
  tsp-solver ./smoke-test.sh
```