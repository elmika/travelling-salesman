# Architecture

This project uses **Hexagonal Architecture** (Ports and Adapters) to keep the core solving logic independent of file I/O, configuration, and presentation.

## Target Architecture

```mermaid
flowchart TB
    subgraph driving [Driving Adapters]
        CLI[TspCli]
    end
    
    subgraph application [Application]
        UseCase[SolveTspUseCase]
    end
    
    subgraph domain [Domain]
        Problem[Problem]
        Solution[Solution]
    end
    
    subgraph ports [Outbound Ports]
        ConfigLoader[ConfigLoader]
        ProblemProvider[ProblemProvider]
    end
    
    subgraph infra [Infrastructure]
        JsonConfig[JsonFileConfigLoader]
        InMemFactory[InMemoryProblemFactory]
    end
    
    CLI --> UseCase
    UseCase --> Problem
    UseCase --> Solution
    UseCase --> ConfigLoader
    UseCase --> ProblemProvider
    ConfigLoader -.->|implements| JsonConfig
    ProblemProvider -.->|implements| InMemFactory
```

## Layers

| Layer | Role | Key types |
|-------|------|-----------|
| **Domain** | Pure business concepts, no I/O | `Problem`, `Solution`, `EuclideanProblem`, `DistanceMatrixProblem`, `domain.geometry.Point` |
| **Application** | Ports (interfaces), use cases, validation | `TspSolver`, `ConfigLoader`, `ProblemProvider`, `SolveTspUseCase`, `SolverStrategy`, `BruteForceSolver`, `RandomSolver`, `SolverConfiguration`, `ProblemTypeParser`, `PermutationsIterator` |
| **Infrastructure** | Adapters that implement ports | `JsonFileConfigLoader`, `InMemoryProblemFactory`, `JSONParsing`, `ProblemFactory` |
| **Adapter (driving)** | Entry point, wiring, presentation | `TspCli`, `TravellingSalesman` (composition root), `RouteCoordinatesMapper`, `RouteCoordinatesView`, `RouteCoordinatesJsonExporter` |

## Dependency Rule

- **Domain** has no dependencies on other packages.
- **Application** depends only on domain and its own ports.
- **Infrastructure** implements application ports; depends on application and domain.
- **Driving adapters** depend on application (and infrastructure only for wiring in `main`).

## Flow

1. **TravellingSalesman.main** (composition root) instantiates adapters and the use case, then calls `TspCli.run()`.
2. **TspCli** loads configuration via `ConfigLoader`, validates it via `SolverConfiguration.createFrom()`, creates a problem via `ProblemProvider`, invokes `TspSolver.solve()`, and prints the solution.
3. **SolveTspUseCase** resolves the strategy name to a `SolverStrategy` (`BruteForceSolver` or `RandomSolver`) and delegates to it. Each strategy returns a `Solution`.
4. For Euclidean problems, **TspCli** exports the route to `output/route.json` via `RouteCoordinatesMapper` and `RouteCoordinatesJsonExporter` for optional visualization (e.g., `index.html`).
5. All file I/O and JSON parsing stays in infrastructure; the application core stays pure.
