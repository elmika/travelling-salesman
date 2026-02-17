## Gaps

- No tests for ProblemFactory, DistanceMatrixProblem, or TravellingSalesman main flow
- EuclideanProblem uses InvalidParameterException but tests expect IllegalArgumentException (potential mismatch)
- JSONParsingTest depends on file.json; JSONParsing.getConfig() for problemConfiguration.json is not tested

## Some more general considerations

Here are some general considerations from a staff-engineer perspective:

### Strengths

- **Separation of concerns**: `Problem` interface with Euclidean vs distance-matrix implementations keeps distance logic isolated.
- **Tests**: JUnit 5 with parameterized tests; tests cover core logic (solver, permutations, Euclidean).
- **Config-driven**: Problem and strategy selected via JSON instead of hardcoding.
- **Docker setup**: Simple, repeatable environment for build and run.

---

### Technical issues

1. **`ProblemFactory.sizeOfProblemType`** – Returns `null` for `int`, which does not compile. Either the return type is actually `Integer` or this should return a sentinel (e.g. `-1` or `0`) and handle it explicitly.
2. **Exception mismatch** – `EuclideanProblem` throws `InvalidParameterException`, but tests expect `IllegalArgumentException`. One of them should be aligned.
3. **`JSONParsing`** – On error it logs to stdout and returns `null`; callers can NPE. A failing `getConfig()` should throw or return a clear error type rather than `null`.
4. **`displaySolution`** – Uses string concatenation in a loop; `StringBuilder` would be more appropriate for any non-trivial output.

---

### Design and maintainability

- **Static wiring** – `JSONParsing.getConfig()`, `ProblemFactory.createProblem()` are static. Injecting ports (as in the hexagonal plan) will make testing and swapping implementations easier.
- **No explicit Solution model** – `Integer[]` plus `getTotalDistance()` couples solver and presentation. A `Solution` value object would clarify the contract and simplify changes.
- **ProblemFactory size limits** – `cities` limited to 15, random to 150. Either document why or make limits configurable.

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
- You might deprecate or reduce visibility of SimpleSolver.getTotalDistance(Integer[]) once you’re sure nothing external needs it, pushing callers to rely solely on Solution.

If you’re happy with this design and your local mvn test run passes, we can next look at further cleanups (e.g., better error handling around JSONParsing, more tests for ProblemFactory, or starting on the hexagonal architecture refactor) in similarly small, focused steps.