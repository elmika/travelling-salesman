## Gaps (remaining)

- No tests for DistanceMatrixProblem or TravellingSalesman main flow
- JSONParsingTest depends on `file.json` in project root; `JSONParsing.getConfig()` for `problemConfiguration.json` is not tested
- ProblemFactory has one test; more per problem type would reduce regression risk

## Addressed (past refactors)

- **sizeOfProblemType** – `ProblemTypeParser.sizeOfProblemType()` returns `0` for no trailing digits
- **Exception mismatch** – `EuclideanProblem` and geometry types throw `IllegalArgumentException`
- **Solution model** – `Solution` value object introduced; solvers return it directly
- **displaySolution** – Uses `StringBuilder` in `TspCli`
- **org.json** – Project uses Jackson; no org.json dependency
- **SimpleSolver** – Removed; `BruteForceSolver` and `RandomSolver` implement `SolverStrategy`
- **ProblemFactory size parsing** – `ProblemTypeParser` validates size at problem creation; `SolverConfiguration` validates strategy and non-blank problem
- **ProblemFactory size limits** – Documented in `ProblemTypeParser` (cities 1–15, random 1–150)

---

## Highest value items to address next

| Priority | Item | Why |
|----------|------|-----|
| 1 | **Silent fallback to defaults** | `getConfig()` falls back to defaults on missing/malformed file; hides misconfiguration in production |
| 2 | **JSONParsingTest brittleness** | Depends on `file.json` in root; move to `src/test/resources/` or temp file |
| 3 | **DistanceMatrixProblem validation** | No validation of matrix shape/indices; invalid input → `ArrayIndexOutOfBoundsException` |
| 4 | **maven-surefire-plugin 3.0.0-M5** | Milestone release; upgrade for CI stability |
| 5 | **Static wiring** | `JSONParsing`, `ProblemFactory` are static; harder to test and swap implementations |

---

## Some more general considerations

### Strengths

- **Separation of concerns**: `Problem` interface with Euclidean vs distance-matrix implementations keeps distance logic isolated.
- **Tests**: JUnit 5 with parameterized tests; tests cover core logic (solver, permutations, Euclidean).
- **Config-driven**: Problem and strategy selected via JSON instead of hardcoding.
- **Docker setup**: Simple, repeatable environment for build and run.

---

### Technical issues (open)

1. **`JSONParsing`** – On missing/malformed config, falls back to defaults without surfacing an error. Callers receive a valid config; production misconfiguration can go unnoticed.
2. **`getConfig()` fallback** – Consider fail-fast or explicit error type instead of silent defaults.

---

### Design and maintainability

- **Static wiring** – `JSONParsing.getConfig()`, `ProblemFactory.createProblem()` are static. Injecting ports would improve testability and flexibility.
- **ProblemFactory size limits** – Documented in `ProblemTypeParser`; could be made configurable if needed.

---

### Testing and resilience

- **`JSONParsingTest`** – Depends on `file.json` in project root. Prefer `src/test/resources/` or temporary file.
- **ProblemFactory** – One test; more per problem type would reduce regression risk.
- **Silent fallback** – `getConfig()` fallback hides misconfiguration; consider warning or metric.

---

### Dependencies and security

- **maven-surefire-plugin:3.0.0-M5** – Milestone release; upgrade to stable for predictable CI.

---

### Configuration robustness

- **Silent fallback to defaults**: `JSONParsing.getConfig()` logs and falls back when `problemConfiguration.json` is missing or malformed. Convenient locally; can hide production misconfiguration.
- **Improvement**: Elevate log severity, surface clear error for non-dev environments, or add metrics around "default config used".

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
