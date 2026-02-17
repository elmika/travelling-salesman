## Gaps

- No tests for ProblemFactory, DistanceMatrixProblem, or TravellingSalesman main flow
- EuclideanProblem uses InvalidParameterException but tests expect IllegalArgumentException (potential mismatch)
- JSONParsingTest depends on file.json; JSONParsing.getConfig() for problemConfiguration.json is not tested