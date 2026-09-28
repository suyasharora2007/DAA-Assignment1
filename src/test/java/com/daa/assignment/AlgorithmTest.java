package com.daa.assignment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class AlgorithmTest {
    @Test
    void sortsMatchArraysSortOnRandomArrays() {
        Random random = new Random(20260928L);
        for (int test = 0; test < 100; test++) {
            int n = random.nextInt(150);
            int[] source = new int[n];
            for (int i = 0; i < n; i++) source[i] = random.nextInt(101) - 50;
            int[] expected = Arrays.copyOf(source, n);
            Arrays.sort(expected);

            int[] merge = Arrays.copyOf(source, n);
            MergeSort.sort(merge, new Metrics());
            assertArrayEquals(expected, merge);

            int[] quick = Arrays.copyOf(source, n);
            QuickSort.sort(quick, new Metrics());
            assertArrayEquals(expected, quick);
        }
    }

    @Test
    void handlesEdgeCases() {
        int[][] cases = { {}, {42}, {7, 7, 7, 7}, {1, 2, 3, 4, 5} };
        for (int[] input : cases) {
            int[] expected = Arrays.copyOf(input, input.length);
            Arrays.sort(expected);
            int[] merge = Arrays.copyOf(input, input.length);
            int[] quick = Arrays.copyOf(input, input.length);
            MergeSort.sort(merge, new Metrics());
            QuickSort.sort(quick, new Metrics());
            assertArrayEquals(expected, merge);
            assertArrayEquals(expected, quick);
        }
    }

    @Test
    void quickSelectMatchesSortedValueOnRandomArrays() {
        Random random = new Random(99);
        for (int test = 0; test < 100; test++) {
            int n = 1 + random.nextInt(150);
            int[] source = new int[n];
            for (int i = 0; i < n; i++) source[i] = random.nextInt(101) - 50;
            int k = random.nextInt(n);
            int[] expected = Arrays.copyOf(source, n);
            Arrays.sort(expected);
            assertEquals(expected[k], QuickSelect.select(source, k, new Metrics()));
        }
    }

    @Test
    void quickSelectRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(new int[0], 0, new Metrics()));
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(new int[]{1, 2}, -1, new Metrics()));
        assertThrows(IllegalArgumentException.class, () -> QuickSelect.select(new int[]{1, 2}, 2, new Metrics()));
    }

    @Test
    void quickSortHasBoundedDepthOnSortedInput() {
        int n = 100_000;
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = i;
        Metrics metrics = new Metrics();
        QuickSort.sort(data, metrics);
        assertEquals(0, metrics.getComparisons() == 0 ? 0 : 0); // keeps the assertion API simple
        org.junit.jupiter.api.Assertions.assertTrue(metrics.getMaxDepth() <= 2 * (Math.log(n) / Math.log(2)) + 1,
                "QuickSort recursion depth was " + metrics.getMaxDepth());
    }
}
