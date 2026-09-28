package com.daa.assignment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

/** Runs the assignment benchmark and writes median results to results.csv. */
public final class Benchmark {
    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final int RUNS = 5;
    private static final long SEED = 20260928L;

    private Benchmark() {
    }

    public static void main(String[] args) throws Exception {
        Path output = args.length > 0 ? Paths.get(args[0]) : Paths.get("results.csv");
        Files.createDirectories(output.toAbsolutePath().getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("algorithm,input,n,time_ms,comparisons,max_depth");
            writer.newLine();

            for (int n : SIZES) {
                for (InputType inputType : InputType.values()) {
                    int[] base = createInput(n, inputType);
                    runSort(writer, "MergeSort", inputType, base, 0);
                    runSort(writer, "QuickSort", inputType, base, 0);
                    runSelect(writer, inputType, base);
                    System.out.printf(Locale.US, "Finished n=%d input=%s%n", n, inputType.name().toLowerCase());
                }
            }
        }

        System.out.println("Benchmark complete: " + output.toAbsolutePath());
    }

    private static void runSort(BufferedWriter writer, String algorithm, InputType inputType,
                                int[] base, int ignored) throws IOException {
        double[] times = new double[RUNS];
        long[] comparisons = new long[RUNS];
        int[] depths = new int[RUNS];

        // Use identical input for each algorithm/run type while preserving the source data.
        for (int run = 0; run < RUNS; run++) {
            int[] copy = Arrays.copyOf(base, base.length);
            Metrics metrics = new Metrics();
            if (algorithm.equals("MergeSort")) {
                MergeSort.sort(copy, metrics);
            } else {
                QuickSort.sort(copy, metrics);
            }
            if (!isSorted(copy)) {
                throw new AssertionError(algorithm + " failed for " + inputType);
            }
            times[run] = metrics.getTimeMillis();
            comparisons[run] = metrics.getComparisons();
            depths[run] = metrics.getMaxDepth();
        }

        writeMedian(writer, algorithm, inputType, base.length, times, comparisons, depths);
    }

    private static void runSelect(BufferedWriter writer, InputType inputType, int[] base) throws IOException {
        double[] times = new double[RUNS];
        long[] comparisons = new long[RUNS];
        int[] depths = new int[RUNS];
        int k = base.length / 2;

        for (int run = 0; run < RUNS; run++) {
            int[] copy = Arrays.copyOf(base, base.length);
            int[] expected = Arrays.copyOf(base, base.length);
            Arrays.sort(expected);
            int expectedValue = expected[k];

            Metrics metrics = new Metrics();
            int actual = QuickSelect.select(copy, k, metrics);
            if (actual != expectedValue) {
                throw new AssertionError("QuickSelect failed for " + inputType);
            }
            times[run] = metrics.getTimeMillis();
            comparisons[run] = metrics.getComparisons();
            depths[run] = metrics.getMaxDepth();
        }

        writeMedian(writer, "QuickSelect", inputType, base.length, times, comparisons, depths);
    }

    private static void writeMedian(BufferedWriter writer, String algorithm, InputType inputType, int n,
                                    double[] times, long[] comparisons, int[] depths) throws IOException {
        Arrays.sort(times);
        Arrays.sort(comparisons);
        Arrays.sort(depths);
        double medianTime = times[times.length / 2];
        long medianComparisons = comparisons[comparisons.length / 2];
        int medianDepth = depths[depths.length / 2];
        writer.write(String.format(Locale.US, "%s,%s,%d,%.6f,%d,%d%n",
                algorithm, inputType.name().toLowerCase(), n, medianTime, medianComparisons, medianDepth));
    }

    private static int[] createInput(int n, InputType type) {
        int[] array = new int[n];
        Random random = new Random(SEED + n * 31L + type.ordinal());
        if (type == InputType.RANDOM) {
            for (int i = 0; i < n; i++) array[i] = random.nextInt();
        } else if (type == InputType.SORTED) {
            for (int i = 0; i < n; i++) array[i] = i;
        } else {
            for (int i = 0; i < n; i++) array[i] = random.nextInt(10);
        }
        return array;
    }

    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) return false;
        }
        return true;
    }

    private enum InputType { RANDOM, SORTED, DUPLICATES }
}
