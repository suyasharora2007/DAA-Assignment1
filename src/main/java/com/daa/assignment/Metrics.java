package com.daa.assignment;

/** Stores performance information for one algorithm run. */
public class Metrics {
    private long comparisons;
    private int currentDepth;
    private int maxDepth;
    private long startTime;
    private long elapsedTime;

    public void reset() {
        comparisons = 0;
        currentDepth = 0;
        maxDepth = 0;
        startTime = 0L;
        elapsedTime = 0L;
    }

    public Metrics() {
        reset();
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        elapsedTime = System.nanoTime() - startTime;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void addComparisons(long count) {
        comparisons += count;
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public double getTimeMillis() {
        return elapsedTime / 1_000_000.0;
    }
}
