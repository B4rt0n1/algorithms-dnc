package com.dnc.algorithms.metrics;

public class MetricsTracker {
    private long comparisons;
    private long allocations;
    private int recursionDepth;
    private int maxRecursionDepth;
    
    public void incrementComparisons() { comparisons++; }
    public void incrementAllocations(long count) { allocations += count; }
    public void enterRecursion() { 
        recursionDepth++; 
        maxRecursionDepth = Math.max(maxRecursionDepth, recursionDepth);
    }
    public void exitRecursion() { recursionDepth--; }
    
    // Getters and reset method
    public void reset() {
        comparisons = 0;
        allocations = 0;
        recursionDepth = 0;
        maxRecursionDepth = 0;
    }
}