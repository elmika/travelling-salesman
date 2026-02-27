# CLAUDE.md — Project instructions for Claude Code

## Runtime environment

The project runs entirely in Docker. There is no local Java or Maven installation to rely on.

**Build the image** (required after any source change before running):
```
docker build -t tsp-solver .
```

**Run the solver**:
```
docker run --rm -v "$(pwd)/output:/app/output" tsp-solver
```

**Run the tests** (must be done inside the container):
```
docker run --rm tsp-solver mvn test
```

Do not attempt to run `mvn` commands directly on the host — they will fail.

## Architecture rules

This project uses Hexagonal Architecture. The dependency rule is strict:

- **Domain** → no imports from any other layer
- **Application** → may import domain only
- **Infrastructure** → may import application and domain
- **Adapter (CLI)** → may import application; may import infrastructure only in the composition root (`TravellingSalesman.java`)

Never collapse these layers or introduce cross-layer shortcuts.

## Solver conventions

- Improvement solvers (2-opt, Or-opt, SA, crossing elimination) are **decorators**: they wrap a `SolverStrategy` and refine its output. They must not contain their own initial-solution logic.
- New solvers must be registered in `SolveTspUseCase` (strategy name → instance mapping) and listed in `problemConfiguration.json` under `possibleResolutionStrategies`.

## Fail-fast philosophy

- No silent defaults. If config is missing, malformed, or contains an unknown strategy, throw — do not fall back to a default.
- Validation belongs in `SolverConfiguration.createFrom()`, not scattered across callers.

## Living documentation

After completing any task, keep the following files in sync:

**README.md** — update whenever behaviour visible to a user changes: new or removed endpoints, changed CLI invocation, new strategy names, changed Docker commands, or any other externally observable difference.

**openapi.yml and tsp-solver.postman_collection.json** — update both together whenever the REST API changes in any way:
- New or removed endpoint
- Changed request or response field (name, type, optionality)
- New, removed, or renamed strategy added to an `enum`
- Changed HTTP status code or error message wording

The Postman collection must stay runnable end-to-end: if a request body or example response in the collection would no longer work against the real server, fix it before committing.
