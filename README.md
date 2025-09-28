## Report: Divide-and-Conquer Algorithms (Assignment 1)

This repository implements a small collection of divide-and-conquer and classical algorithms along with measurement utilities and a simple command-line interface. The following report summarizes the task, the repository structure, the responsibilities of the main components, how to build and run the project, test coverage, and how the implementation satisfies the assignment requirements.

## Short explanation of the code

Top-level classes (location: `src/main/java/com/dnc/algorithms`):

- `Main.java` — simple entry point used when running packaged JARs. It delegates to `CLI` or the test harness depending on arguments.
- `CLI.java` — command-line interface that parses user arguments, selects algorithms to run, triggers experiments, and writes CSV results.
- `TestRunner.java` — small harness used to run correctness and micro-benchmark experiments for the assignment.

Packages and their responsibilities:

- `geometry/ClosestPair.java` — divide-and-conquer algorithm that computes the closest pair of points in the plane. Uses the classic O(n log n) approach (sort by x, recursive split, and strip check).
- `select/DeterministicSelect.java` — implementation of the deterministic linear-time selection (median-of-medians) algorithm.
- `sort/MergeSort.java` and `sort/QuickSort.java` — comparison-based sorts. `MergeSort` is the stable divide-and-conquer merge sort; `QuickSort` is a standard implementation used for experiments and comparison.
- `metrics/MetricsTracker.java` — lightweight instrumentation helper to track comparisons, swaps, and elapsed time. Algorithms report metrics here, which are later written to CSV.
- `util/ArrayUtils.java` — small helpers for generating input arrays, copying, and validating outputs.
- `util/CSVWriter.java` — writes experiment results to CSV for later analysis.

Design notes:
- Each algorithm is self-contained and returns both result and a metrics object. This makes it easy to run the same input across algorithms and compare measured costs.
- The CLI/TestRunner are thin wrappers: they generate inputs (random or deterministic), run the chosen algorithm(s), collect metrics, and persist results.

## Contract (inputs/outputs, error modes, success criteria)

- Inputs: algorithm choice, input size or explicit input data, random seed (optional), output CSV path.
- Outputs: algorithm result (e.g., nearest pair or selected element), metrics (comparisons, swaps, time), CSV file with one row per experiment.
- Error modes: invalid args, impossible selection indices, malformed inputs. The CLI validates arguments and prints usage.
- Success: algorithm returns expected result on unit tests and produces a CSV when run via CLI/TestRunner.

## Edge cases considered

- Empty and single-element inputs (closest pair should handle by returning an empty/neutral result or throwing a well-documented exception depending on the assignment requirement).
- Duplicate values and duplicate points.
- Very small arrays (n <= 3) where base cases are used.
- Large arrays for performance experiments — code uses efficient recursion and avoids unnecessary copies where possible.

## How to build and run

This project uses Maven. From the repository root (`algorithms-dnc`) run:

mvn -q exec:java -Dexec.mainClass="com.dnc.algorithms.TestRunner" -Dexec.


## Files of interest (quick map)

- `src/main/java/com/dnc/algorithms/CLI.java` — CLI front-end and argument parsing.
- `src/main/java/com/dnc/algorithms/TestRunner.java` — experiment harness and example runs.
- `src/main/java/com/dnc/algorithms/geometry/ClosestPair.java` — divide-and-conquer closest-pair implementation.
- `src/main/java/com/dnc/algorithms/select/DeterministicSelect.java` — deterministic selection.
- `src/main/java/com/dnc/algorithms/sort/MergeSort.java` and `QuickSort.java` — sorts used for comparisons.
- `src/main/java/com/dnc/algorithms/metrics/MetricsTracker.java` — metrics collection.
- `src/main/java/com/dnc/algorithms/util/ArrayUtils.java` and `CSVWriter.java` — helpers.