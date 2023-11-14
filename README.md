# Travelling Salesman Project solver

TSP is a classic algorithmic problem in the field of computer science and operations research. It asks for the shortest possible route that visits a set of locations exactly once and returns to the origin point.

In this project, we implement some problems and algorithms to resolve them.


## Project Structure

This is a typical Maven structure:

```
ProjectRoot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourname/tsp/
│   │   │       └── TravelingSalesman.java
│   │   └── resources/
│   └── test/
│       └── java/
└── pom.xml
└── Dockerfile
```

## Running with Docker

You can also run this application using Docker to avoid installing Maven and Java directly on your machine.

### Building the Docker Image

To build the Docker image, run:

```bash
docker build -t tsp-solver .
```

Then, run the application:

```bash
docker run tsp-solver
```