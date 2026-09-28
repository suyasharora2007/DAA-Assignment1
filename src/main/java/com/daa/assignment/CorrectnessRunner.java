package com.daa.assignment;

import java.util.Arrays;
import java.util.Random;

/** Lightweight local correctness runner; Maven/JUnit tests remain the official test suite. */
public final class CorrectnessRunner {
    public static void main(String[] args) {
        Random random = new Random(20260928L);
        for (int test = 0; test < 150; test++) {
            int n = random.nextInt(80);
            int[] source = new int[n];
            for (int i = 0; i < n; i++) source[i] = random.nextInt(21) - 10;

            int[] expected = Arrays.copyOf(source, n);
            Arrays.sort(expected);

            int[] merge = Arrays.copyOf(source, n);
            MergeSort.sort(merge, new Metrics());
            assert Arrays.equals(expected, merge);

            int[] quick = Arrays.copyOf(source, n);
            QuickSort.sort(quick, new Metrics());
            assert Arrays.equals(expected, quick);

            if (n > 0) {
                int k = random.nextInt(n);
                int[] select = Arrays.copyOf(source, n);
                int actual = QuickSelect.select(select, k, new Metrics());
                assert actual == expected[k];
            }
        }

        int[] edge = {};
        MergeSort.sort(edge, new Metrics());
        QuickSort.sort(edge, new Metrics());

        boolean threw = false;
        try {
            QuickSelect.select(new int[0], 0, new Metrics());
        } catch (IllegalArgumentException expected) {
            threw = true;
        }
        assert threw;

        System.out.println("CorrectnessRunner: all checks passed.");
    }
}
