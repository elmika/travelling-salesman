#!/usr/bin/env bash
set -euo pipefail

# Simple smoke test: build the shaded JAR and run it once.
# This is intended to be run either locally (if you have Java and Maven)
# or inside the Docker image, e.g.:
#   docker run tsp-solver ./smoke-test.sh

VERSION=$(mvn -q -DskipTests -Dexpression=project.version -DforceStdout help:evaluate)

echo "Building project (version: ${VERSION})..."
mvn -q -DskipTests package

JAR="target/tsp-solver-${VERSION}.jar"

if [[ ! -f "${JAR}" ]]; then
  echo "Smoke test failed: JAR not found at ${JAR}" >&2
  exit 1
fi

echo "Running smoke test with ${JAR}..."
java -jar "${JAR}" | head -n 5

echo "Smoke test completed."

