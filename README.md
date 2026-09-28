# DAA Assignment 1 — Divide and Conquer

This Maven project implements MergeSort, QuickSort and QuickSelect and measures time, comparisons and recursion depth.

## Build and test

```bash
mvn clean test
```

## Run the benchmark

```bash
mvn -q -DskipTests package
java -cp target/classes com.daa.assignment.Benchmark
```

The benchmark creates `results.csv` with the required columns:

`algorithm,input,n,time_ms,comparisons,max_depth`

It uses n = 1,000, 10,000, 100,000 and 1,000,000; random, sorted and duplicate-heavy inputs; and the median of five runs.

## Generate plots

After the benchmark:

```bash
python scripts/plot_results.py
```

The PNG files are written to `plots/`.

## Git workflow required by the assignment

- `main`
- `feature/mergesort`
- `feature/quicksort`
- `feature/select`
- `feature/metrics`
- release tag: `v1.0`

Meaningful commits are used for each feature and the final release is merged into `main`.
