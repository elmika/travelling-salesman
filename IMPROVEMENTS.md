## Gaps

- No tests for DistanceMatrixProblem or TravellingSalesman main flow
- JSONParsingTest depends on file.json; JSONParsing.getConfig() for problemConfiguration.json is not tested

## Addressed (refactor 2025)

- **Solution introduction**: `Solution` value object introduced; solvers return `Solution`.
- **sizeOfProblemType behavior**: Returns `0` for no trailing digits; `ProblemTypeParser.validateProblemType()` enforces valid sizes at config load time.
- **Config validation**: `SolverConfiguration.createFrom()` validates `problem` and `resolutionStrategy`; fails fast with clear messages.
- **Solver extraction**: `BruteForceSolver` and `RandomSolver` implement `SolverStrategy`; `SimpleSolver` removed.

## Some more general considerations

Here are some general considerations from a staff-engineer perspective:

### Strengths

- **Separation of concerns**: `Problem` interface with Euclidean vs distance-matrix implementations keeps distance logic isolated.
- **Tests**: JUnit 5 with parameterized tests; tests cover core logic (solver, permutations, Euclidean).
- **Config-driven**: Problem and strategy selected via JSON instead of hardcoding.
- **Docker setup**: Simple, repeatable environment for build and run.

---

### Technical issues

1. ~~**`ProblemFactory.sizeOfProblemType`**~~ – **Addressed**: Now `ProblemTypeParser.sizeOfProblemType()` returns `0` for no trailing digits; validation in `SolverConfiguration`.
2. ~~**Exception mismatch**~~ – **Addressed**: `EuclideanProblem` and geometry types throw `IllegalArgumentException`.
3. **`JSONParsing`** – On error it logs to stdout and returns `null`; callers can NPE. A failing `getConfig()` should throw or return a clear error type rather than `null`.
4. **`displaySolution`** – Uses string concatenation in a loop; `StringBuilder` would be more appropriate for any non-trivial output.

---

### Design and maintainability

- **Static wiring** – `JSONParsing.getConfig()`, `ProblemFactory.createProblem()` are static. Injecting ports (as in the hexagonal plan) will make testing and swapping implementations easier.
- ~~**No explicit Solution model**~~ – **Addressed**: `Solution` value object introduced.
- **ProblemFactory size limits** – `cities` 8–15, random 1–150 (see `ProblemTypeParser`); limits are in code and validated at config load.

---

### Testing and resilience

- **`JSONParsingTest`** – Depends on `file.json` in the project root. Prefer a test resource (e.g. `src/test/resources/`) or a temporary file to avoid coupling to a real config file.
- **No tests for ProblemFactory** – Creation logic is nontrivial (parsing, limits). At least one test per problem type would reduce regression risk.
- **Silent fallback** – `getConfig()` falling back to defaults on error can hide misconfiguration. A warning or metric on fallback would help.

---

### Dependencies and security

- **org.json:20090211** – Very old. Consider `jakarta.json` or Jackson if you want maintained JSON support and fewer security concerns.
- **maven-surefire-plugin:3.0.0-M5** – Milestone release. Worth moving to a stable Surefire version for predictable CI runs.

---

### Suggested order of work

1. Fix the `sizeOfProblemType` return-type/null handling.
2. Align exception type and tests in `EuclideanProblem`.
3. Introduce a `Solution` type and use it as the solver’s return value.
4. Move toward hexagonal architecture (ports/adapters) if you want better testability and flexibility.

## SizeOfProblem future improvements

- Risk: Treating “no digits” as size 0 means misconfigured values like "cities" or "fully-random" without a number will now produce an IllegalArgumentException via the existing size checks, rather than any implicit default. If you intended a default size in those cases, we’d need to adjust sizeOfProblemType or createProblem accordingly.

Future improvement ideas:

- Add more targeted tests for invalid size strings (e.g. "cities0", "cities-1", "citiesXYZ") to explicitly document/lock in behavior.
- If you later want user-friendly config errors, we could have sizeOfProblemType throw a descriptive exception when no digits are present instead of returning 0.

## Solution object introduction: Future improvements

- Consider adding factory methods or builders on Solution if you later want to attach metadata (e.g., algorithm type, iteration count, time to compute).
- ~~**SimpleSolver**~~ – **Addressed**: Removed; BruteForceSolver and RandomSolver return Solution directly.

If you’re happy with this design and your local mvn test run passes, we can next look at further cleanups (e.g., better error handling around JSONParsing, more tests for ProblemFactory, or starting on the hexagonal architecture refactor) in similarly small, focused steps.

---

## Additional risks and improvement ideas (current state)

### Configuration robustness

- **Silent fallback to defaults**: `JSONParsing.getConfig()` logs and falls back to default `ProblemConfiguration` values when `problemConfiguration.json` is missing or malformed. This is convenient locally but can hide misconfiguration in production.
- **Improvement**: Consider elevating log severity, surfacing a clear error (e.g. custom exception or result type) for non-development environments, or adding metrics around “default config used” events.

### Scaling and performance

- **Brute-force strategy**: `BruteForceSolver`’s `"brute-force"` strategy enumerates permutations (with fixed starting city) and will not scale beyond small `Problem.getSize()`; factorial growth makes it impractical for larger instances.
- **Improvement**: For larger problem sizes, introduce heuristics (e.g. nearest neighbor, 2-opt, simulated annealing) or explicitly document supported size ranges for each strategy.

### Validation and correctness

- **DistanceMatrixProblem validation**: `DistanceMatrixProblem` assumes valid indices and a square matrix; invalid inputs lead to array-index exceptions.
- ~~**ProblemFactory size parsing**~~ – **Addressed**: `ProblemTypeParser.validateProblemType()` called from `SolverConfiguration.createFrom()`; invalid sizes fail at config load with clear messages.

### Hexagonal architecture and wiring

- **Static utilities in infrastructure**: `JSONParsing` and `ProblemFactory` are static and referenced directly from ports’ implementations. This works but makes tests depend on global state and concrete file names.
- **Improvement**: Over time, move more logic behind explicit ports (e.g. `ConfigurationRepository`, `ProblemCatalog`) and inject them into the application layer. This will improve testability, enable alternative backends, and better align with the hexagonal architecture described in `ARCHITECTURE.md`.