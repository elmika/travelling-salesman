# Travelling Salesman Problem Solver

TSP is a classic algorithmic problem in the field of computer science and operations research. It asks for the shortest possible route that visits a set of locations exactly once and returns to the origin point.

This project implements a set of resolution and improvement algorithms exposed via a **Spring Boot REST API** and an optional **CLI**.

## Architecture

The project follows **Hexagonal Architecture**. See [ARCHITECTURE.md](ARCHITECTURE.md) for the target architecture diagram and dependency rules.

## Project Structure

```
src/main/java/com/elmika/tsp/
├── domain/
│   ├── problem/      Problem, EuclideanProblem, DistanceMatrixProblem, Point
│   └── solution/     Solution
├── application/
│   ├── resolution/   ResolutionStrategy, BruteForceSolver, RandomSolver,
│   │                 NearestNeighborSolver, GreedyEdgeSolver, PermutationsIterator
│   ├── improvement/  ImprovementStrategy, TwoOptSolver, OrOptSolver,
│   │                 SimulatedAnnealingSolver, CrossingEliminationSolver
│   ├── benchmark/    BenchmarkTspUseCase, TspBenchmark, BenchmarkResult
│   └── (root)        TspSolver, SolveTspUseCase, ProblemProvider,
│                     SolverConfiguration, ProblemTypeParser
├── infrastructure/   JsonFileConfigLoader, InMemoryProblemFactory,
│                     ProblemFactory, JSONParsing
└── adapter/
    ├── api/          TspApiApplication, TspApiConfig, controllers, dto/
    ├── cli/          TravellingSalesman (composition root), TspCli
    └── view/         RouteCoordinatesMapper, RouteCoordinatesJsonExporter
```

## Running with Docker

### Build the image

```bash
docker build -t tsp-solver .
```

### Start the API server

```bash
docker run --rm -p 8080:8080 tsp-solver
```

The server starts on port 8080. All four endpoints are immediately available — no configuration file needed.

### Run the tests

```bash
docker run --rm tsp-solver mvn test
```

---

## REST API

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET`  | `/api/problems/{type}` | Retrieve a named pre-built problem as a list of 2D points |
| `POST` | `/api/solve`           | Construct a tour from scratch using a resolution strategy |
| `POST` | `/api/improve`         | Improve an existing tour using an improvement strategy |
| `POST` | `/api/benchmark`       | Rank a set of pre-computed results by distance |

**Resolution strategies** (for `/api/solve`):
`brute-force`, `random`, `random10`, `random100`, `nearest-neighbor`, `greedy-edge`

**Improvement strategies** (for `/api/improve`):
`2opt`, `oropt`, `sa`, `uncrossing`

**Problem types** (for `/api/problems/{type}`):
`cities8` … `cities15`, `fully-random1` … `fully-random150`, `partially-random1` … `partially-random150`, `euclidean`

All error responses (unknown strategy, invalid input) return HTTP 400 with a JSON body:
```json
{ "error": "reason" }
```

---

### Typical workflow

This example walks through a complete session: fetch a problem, solve it with two
strategies, improve the better result, then benchmark all three.

**1. Start the server**

```bash
docker run --rm -p 8080:8080 tsp-solver
```

**2. Fetch the `cities10` problem**

```bash
curl -s http://localhost:8080/api/problems/cities10 | jq .
```

```json
{
  "points": [
    {"x": 37.0, "y": 95.0}, {"x": 73.0, "y": 59.0}, {"x": 15.0, "y": 15.0},
    {"x": 5.0,  "y": 86.0}, {"x": 60.0, "y": 70.0}, {"x": 2.0,  "y": 96.0},
    {"x": 83.0, "y": 21.0}, {"x": 18.0, "y": 18.0}, {"x": 30.0, "y": 52.0},
    {"x": 43.0, "y": 29.0}
  ]
}
```

**3. Solve with `nearest-neighbor`**

```bash
curl -s -X POST http://localhost:8080/api/solve \
  -H "Content-Type: application/json" \
  -d '{
    "points": [
      {"x":37,"y":95},{"x":73,"y":59},{"x":15,"y":15},{"x":5,"y":86},{"x":60,"y":70},
      {"x":2,"y":96},{"x":83,"y":21},{"x":18,"y":18},{"x":30,"y":52},{"x":43,"y":29}
    ],
    "strategy": "nearest-neighbor"
  }' | jq .
```

```json
{
  "route": [1, 5, 4, 6, 9, 3, 8, 7, 10, 2],
  "totalDistance": 312.45,
  "strategy": "nearest-neighbor",
  "durationMs": 1
}
```

**4. Solve with `greedy-edge`** (save this result too — we'll benchmark it later)

```bash
curl -s -X POST http://localhost:8080/api/solve \
  -H "Content-Type: application/json" \
  -d '{
    "points": [
      {"x":37,"y":95},{"x":73,"y":59},{"x":15,"y":15},{"x":5,"y":86},{"x":60,"y":70},
      {"x":2,"y":96},{"x":83,"y":21},{"x":18,"y":18},{"x":30,"y":52},{"x":43,"y":29}
    ],
    "strategy": "greedy-edge"
  }' | jq .
```

```json
{
  "route": [1, 6, 4, 9, 5, 2, 7, 10, 3, 8],
  "totalDistance": 298.17,
  "strategy": "greedy-edge",
  "durationMs": 3
}
```

**5. Improve the `greedy-edge` solution with `2opt`**

Take the route and distance from step 4 and pass them to `/api/improve`:

```bash
curl -s -X POST http://localhost:8080/api/improve \
  -H "Content-Type: application/json" \
  -d '{
    "points": [
      {"x":37,"y":95},{"x":73,"y":59},{"x":15,"y":15},{"x":5,"y":86},{"x":60,"y":70},
      {"x":2,"y":96},{"x":83,"y":21},{"x":18,"y":18},{"x":30,"y":52},{"x":43,"y":29}
    ],
    "solution": {"route": [1,6,4,9,5,2,7,10,3,8], "totalDistance": 298.17},
    "strategy": "2opt"
  }' | jq .
```

```json
{
  "route": [1, 4, 6, 9, 5, 2, 10, 7, 3, 8],
  "totalDistance": 281.03,
  "strategy": "2opt",
  "originalDistance": 298.17,
  "improvementPercent": 5.7,
  "durationMs": 2
}
```

**6. Benchmark all three results**

Collect the distances from steps 3, 4, and 5 and rank them:

```bash
curl -s -X POST http://localhost:8080/api/benchmark \
  -H "Content-Type: application/json" \
  -d '{
    "solutions": [
      {"label": "nearest-neighbor",       "distance": 312.45, "durationMs": 1},
      {"label": "greedy-edge",             "distance": 298.17, "durationMs": 3},
      {"label": "greedy-edge + 2opt",      "distance": 281.03, "durationMs": 5}
    ]
  }' | jq .
```

```json
{
  "results": [
    {"rank": 1, "label": "greedy-edge + 2opt",  "distance": 281.03, "gapPercent": 0.0,  "durationMs": 5},
    {"rank": 2, "label": "greedy-edge",          "distance": 298.17, "gapPercent": 6.1,  "durationMs": 3},
    {"rank": 3, "label": "nearest-neighbor",     "distance": 312.45, "gapPercent": 11.2, "durationMs": 1}
  ]
}
```

---

## CLI

The CLI reads its configuration from `problemConfiguration.json` and writes the solved
route to `output/route.json`.

To run the CLI instead of the API server:

```bash
docker run --rm \
  -v "$(pwd)/problemConfiguration.json":/app/problemConfiguration.json:ro \
  -v "$(pwd)/output":/app/output \
  tsp-solver \
  mvn -q exec:java -Dexec.mainClass=com.elmika.tsp.adapter.cli.TravellingSalesman
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

  When you run the CLI with a Euclidean problem type, the application exports the last
  solved route to `output/route.json`. Use a named volume to make it available to the viewer:

  ```bash
  docker volume create tsp-output

  docker run --rm \
    -v "$(pwd)/problemConfiguration.json":/app/problemConfiguration.json:ro \
    -v tsp-output:/app/output \
    tsp-solver \
    mvn -q exec:java -Dexec.mainClass=com.elmika.tsp.adapter.cli.TravellingSalesman
  ```

- **Serving the viewer via Docker (nginx)**:

  ```bash
  docker run --rm -p 8000:80 \
    -v tsp-output:/usr/share/nginx/html/output:ro \
    -v "$(pwd)/index.html":/usr/share/nginx/html/index.html:ro \
    nginx:alpine
  ```

  Open `http://localhost:8000/index.html`, enter `output/route.json` as the filename,
  and click **Load route**.
