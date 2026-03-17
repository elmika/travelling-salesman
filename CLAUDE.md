# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Runtime environment

The project runs entirely in Docker. There is no local Java or Maven installation to rely on.

**Build the image** (required after any source change before running):
```
docker build -t tsp-solver .
```

**Start the API server + web UI**:
```
docker run --rm -p 8080:8080 tsp-solver
```

**Run the tests** (uses the dedicated `test` stage which has Maven — the final image does not):
```
docker build --target test -t tsp-solver-test .
```

Do not attempt to run `mvn` commands directly on the host — they will fail.

**Known pre-existing test failure**: `TravellingSalesmanTest.mainFlowRunsWithoutException` reads `problemConfiguration.json` from disk, which does not exist in the Docker build context. All other tests pass.

## Architecture rules

This project uses Hexagonal Architecture. The dependency rule is strict:

- **Domain** → no imports from any other layer
- **Application** → may import domain only
- **Infrastructure** → may import application and domain
- **Adapter (CLI)** → may import application; may import infrastructure only in the composition root (`TravellingSalesman.java`)
- **Adapter (API)** → may import application; may import infrastructure only in the composition root (`TspApiConfig.java`)

Never collapse these layers or introduce cross-layer shortcuts.

## Solver conventions

**Improvement solvers** (TwoOptSolver, OrOptSolver, SimulatedAnnealingSolver, CrossingEliminationSolver) implement **both** `ResolutionStrategy` and `ImprovementStrategy`:
- `(ResolutionStrategy inner)` constructor — for CLI composite use; `solve(problem)` delegates to `improve(problem, inner.solve(problem))`
- `()` no-arg constructor — for API standalone use via `/api/improve`
- The core logic lives in `improve(problem, initial)`; `solve()` must throw if `inner` is null

These are **decorators**: they wrap a `SolverStrategy` and refine its output. They must not contain their own initial-solution logic.

**Adding a new resolution strategy**:
1. Register it in `SolveTspUseCase` (strategy name → instance mapping)
2. Register it in `ResolutionService` (with `syncSizeLimit`/`asyncSizeLimit` via `StrategyEntry`)
3. List it in `problemConfiguration.json` under `possibleResolutionStrategies` (CLI)
4. Add it to the `strategy` enum in `openapi.yml` and `tsp-solver.postman_collection.json`

**Adding a new improvement strategy**: same pattern but in `ImprovementService`.

## Fail-fast philosophy

- No silent defaults. If config is missing, malformed, or contains an unknown strategy, throw — do not fall back to a default.
- Validation belongs in `SolverConfiguration.createFrom()`, not scattered across callers.
- `ProblemFactory` default case throws on unknown type.

## Living documentation

After completing any task, keep the following files in sync:

**README.md** — update whenever behaviour visible to a user changes: new or removed endpoints, changed CLI invocation, new strategy names, changed Docker commands, or any other externally observable difference.

**openapi.yml and tsp-solver.postman_collection.json** — update both together whenever the REST API changes in any way:
- New or removed endpoint
- Changed request or response field (name, type, optionality)
- New, removed, or renamed strategy added to an `enum`
- Changed HTTP status code or error message wording

The Postman collection must stay runnable end-to-end: if a request body or example response in the collection would no longer work against the real server, fix it before committing.
