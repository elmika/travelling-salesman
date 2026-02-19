## Gaps (remaining)

- DistanceMatrixProblem validation: no validation of matrix shape/indices; invalid input → `ArrayIndexOutOfBoundsException`
- maven-surefire-plugin:3.0.0-M5 is a milestone release; upgrade for CI stability

## Addressed (past refactors)

- **Silent fallback to defaults** – `JSONParsing.getConfig()` throws `ConfigLoadException` when the config file is missing or malformed; no silent defaults
- **sizeOfProblemType** – `ProblemTypeParser.sizeOfProblemType()` returns `0` for no trailing digits
- **Exception mismatch** – `EuclideanProblem` and geometry types throw `IllegalArgumentException`
- **Solution model** – `Solution` value object introduced; solvers return it directly
- **displaySolution** – Uses `StringBuilder` in `TspCli`
- **org.json** – Project uses Jackson; no org.json dependency
- **SimpleSolver** – Removed; `BruteForceSolver` and `RandomSolver` implement `SolverStrategy`
- **ProblemFactory size parsing** – `ProblemTypeParser` validates size at problem creation; `SolverConfiguration` validates strategy and non-blank problem
- **ProblemFactory size limits** – Documented in `ProblemTypeParser` (cities 1–15, random 1–150)
- **DistanceMatrixProblem tests** – `DistanceMatrixProblemTest` covers `getSize`, `getDistance`, symmetry, diagonal
- **TravellingSalesman main flow** – `TravellingSalesmanTest` wires CLI, config, problem factory, solver; asserts run completes
- **JSONParsingTest brittleness** – Uses `src/test/resources/file.json`; `getConfig(Path)` overload; tests for valid config and ConfigLoadException on missing/malformed file
- **ProblemFactory coverage** – Tests added for all problem types: trivial, simple, bigger, euclidean, cities7, fully-random10, partially-random5

---

## Highest value items to address next

| Priority | Item | Why |
|----------|------|-----|
| 1 | **DistanceMatrixProblem validation** | No validation of matrix shape/indices; invalid input → `ArrayIndexOutOfBoundsException` |
| 2 | **maven-surefire-plugin 3.0.0-M5** | Milestone release; upgrade for CI stability |
| 3 | **Static wiring** | `JSONParsing`, `ProblemFactory` are static; harder to test and swap implementations |

---

## Some more general considerations

### Strengths

- **Separation of concerns**: `Problem` interface with Euclidean vs distance-matrix implementations keeps distance logic isolated.
- **Tests**: JUnit 5 with parameterized tests; tests cover core logic (solver, permutations, Euclidean), DistanceMatrixProblem, main CLI flow, JSON config, and all ProblemFactory types.
- **Config-driven**: Problem and strategy selected via JSON instead of hardcoding.
- **Docker setup**: Simple, repeatable environment for build and run.

---

### Technical issues (open)

1. ~~**`JSONParsing`** – On missing/malformed config, falls back to defaults without surfacing an error.~~ Addressed: `getConfig()` throws `ConfigLoadException` on missing or malformed file.

---

### Design and maintainability

- **Static wiring** – `JSONParsing.getConfig()`, `ProblemFactory.createProblem()` are static. Injecting ports would improve testability and flexibility.
- **ProblemFactory size limits** – Documented in `ProblemTypeParser`; could be made configurable if needed.

---

### Testing and resilience

- ~~**Silent fallback** – `getConfig()` fallback hides misconfiguration~~ Addressed: throws `ConfigLoadException`.

---

### Dependencies and security

- **maven-surefire-plugin:3.0.0-M5** – Milestone release; upgrade to stable for predictable CI.

---

### Configuration robustness

- ~~**Silent fallback to defaults**: `JSONParsing.getConfig()` logs and falls back when `problemConfiguration.json` is missing or malformed.~~ Addressed: now throws `ConfigLoadException` instead of defaulting.

---

### Scaling and performance

- **Brute-force strategy**: `BruteForceSolver` enumerates permutations; factorial growth limits scalability.
- **Improvement**: Introduce heuristics (nearest neighbor, 2-opt, simulated annealing) or document supported size ranges.

---

### Validation and correctness

- **DistanceMatrixProblem validation**: Assumes valid indices and square matrix; invalid input causes array-index exceptions.
- **Improvement**: Validate matrix shape and indices; throw descriptive exceptions.

---

### Hexagonal architecture and wiring

- **Static utilities**: `JSONParsing` and `ProblemFactory` are static; tests depend on global state and concrete file names.
- **Improvement**: Move logic behind explicit ports (`ConfigurationRepository`, `ProblemCatalog`) and inject into application layer.
