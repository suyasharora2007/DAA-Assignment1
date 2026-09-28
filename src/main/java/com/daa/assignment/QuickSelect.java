package com.daa.assignment;

import java.util.concurrent.ThreadLocalRandom;

/** QuickSelect for the k-th smallest element, where k is zero-based. */
public final class QuickSelect {
    private QuickSelect() {
    }

    public static int select(int[] array, int k, Metrics metrics) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k must be between 0 and array.length - 1");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("Metrics must not be null");
        }

        metrics.reset();
        metrics.startTimer();
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            if (low == high) {
                metrics.stopTimer();
                return array[low];
            }

            int pivotIndex = ThreadLocalRandom.current().nextInt(low, high + 1);
            int pivot = array[pivotIndex];
            int[] equalRange = QuickSort.partition3(array, low, high, pivot, metrics);

            if (k < equalRange[0]) {
                high = equalRange[0] - 1;
            } else if (k > equalRange[1]) {
                low = equalRange[1] + 1;
            } else {
                metrics.stopTimer();
                return array[k];
            }
        }

        // The loop can only reach here for invalid internal state.
        metrics.stopTimer();
        throw new IllegalStateException("QuickSelect failed to locate k");
    }
}
