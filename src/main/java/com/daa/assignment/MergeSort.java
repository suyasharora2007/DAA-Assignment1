package com.daa.assignment;

/** MergeSort with one reusable buffer and an insertion-sort cutoff. */
public final class MergeSort {
    private static final int INSERTION_CUTOFF = 15;

    private MergeSort() {
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
            int[] buffer = new int[array.length];
            sortRange(array, buffer, 0, array.length - 1, metrics);
        }
        metrics.stopTimer();
    }

    private static void sortRange(int[] array, int[] buffer, int left, int right, Metrics metrics) {
        metrics.enterRecursion();
        try {
            if (right - left + 1 <= INSERTION_CUTOFF) {
                insertionSort(array, left, right, metrics);
                return;
            }

            int mid = left + (right - left) / 2;
            sortRange(array, buffer, left, mid, metrics);
            sortRange(array, buffer, mid + 1, right, metrics);

            merge(array, buffer, left, mid, right, metrics);
        } finally {
            metrics.exitRecursion();
        }
    }

    private static void insertionSort(int[] array, int left, int right, Metrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= left) {
                metrics.incrementComparisons();
                if (array[j] <= key) {
                    break;
                }
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    private static void merge(int[] array, int[] buffer, int left, int mid, int right, Metrics metrics) {
        System.arraycopy(array, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            metrics.incrementComparisons();
            if (buffer[i] <= buffer[j]) {
                array[k++] = buffer[i++];
            } else {
                array[k++] = buffer[j++];
            }
        }

        while (i <= mid) {
            array[k++] = buffer[i++];
        }
        while (j <= right) {
            array[k++] = buffer[j++];
        }
    }
}
