package com.daa.assignment;

import java.util.concurrent.ThreadLocalRandom;

/** QuickSort with random pivots, 3-way partitioning and bounded recursion depth. */
public final class QuickSort {
    private QuickSort() {
    }

    public static void sort(int[] array, Metrics metrics) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("Metrics must not be null");
        }

        metrics.reset();
        metrics.startTimer();
        if (array.length > 1) {
            sortRange(array, 0, array.length - 1, metrics);
        }
        metrics.stopTimer();
    }

    private static void sortRange(int[] array, int low, int high, Metrics metrics) {
        while (low < high) {
            int pivotIndex = ThreadLocalRandom.current().nextInt(low, high + 1);
            int pivot = array[pivotIndex];
            int[] equalRange = partition3(array, low, high, pivot, metrics);
            int leftHigh = equalRange[0] - 1;
            int rightLow = equalRange[1] + 1;

            int leftSize = leftHigh - low + 1;
            int rightSize = high - rightLow + 1;

            // Recurse only into the smaller side; iterate over the larger side.
            if (leftSize < rightSize) {
                if (leftSize > 1) {
                    metrics.enterRecursion();
                    try {
                        sortRange(array, low, leftHigh, metrics);
                    } finally {
                        metrics.exitRecursion();
                    }
                }
                low = rightLow;
            } else {
                if (rightSize > 1) {
                    metrics.enterRecursion();
                    try {
                        sortRange(array, rightLow, high, metrics);
                    } finally {
                        metrics.exitRecursion();
                    }
                }
                high = leftHigh;
            }
        }
    }

    /** Returns {equalStart, equalEnd}. */
    static int[] partition3(int[] array, int low, int high, int pivot, Metrics metrics) {
        int less = low;
        int current = low;
        int greater = high;

        while (current <= greater) {
            metrics.incrementComparisons();
            if (array[current] < pivot) {
                swap(array, less++, current++);
            } else {
                metrics.incrementComparisons();
                if (array[current] > pivot) {
                    swap(array, current, greater--);
                } else {
                    current++;
                }
            }
        }
        return new int[]{less, greater};
    }

    private static void swap(int[] array, int i, int j) {
        if (i != j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
