package com.dnc.algorithms.select;

import com.dnc.algorithms.metrics.MetricsTracker;
import com.dnc.algorithms.util.ArrayUtils;

public class DeterministicSelect {
    private final MetricsTracker metrics;
    
    public DeterministicSelect(MetricsTracker metrics) {
        this.metrics = metrics;
    }
    
    public int select(int[] arr, int k) {
        return select(arr, 0, arr.length - 1, k);
    }
    
    private int select(int[] arr, int left, int right, int k) {
        metrics.enterRecursion();
        
        if (left == right) {
            metrics.exitRecursion();
            return arr[left];
        }
        
        int pivotIndex = partitionWithMoM(arr, left, right);
        int pivotRank = pivotIndex - left + 1;
        
        if (k == pivotRank) {
            metrics.exitRecursion();
            return arr[pivotIndex];
        } else if (k < pivotRank) {
            metrics.exitRecursion();
            return select(arr, left, pivotIndex - 1, k);
        } else {
            metrics.exitRecursion();
            return select(arr, pivotIndex + 1, right, k - pivotRank);
        }
    }
    
    private int partitionWithMoM(int[] arr, int left, int right) {
        int median = findMedianOfMedians(arr, left, right);
        int medianIndex = findIndex(arr, left, right, median);
        ArrayUtils.swap(arr, medianIndex, right);
        
        return partition(arr, left, right);
    }
    
    private int findMedianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;
        if (n <= 5) {
            return findMedian(arr, left, right);
        }
        
        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];
        
        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            medians[i] = findMedian(arr, groupLeft, groupRight);
        }
        
        metrics.incrementAllocations(numGroups);
        return select(medians, 0, numGroups - 1, numGroups / 2);
    }
    
    private int findMedian(int[] arr, int left, int right) {
        // Simple insertion sort for small arrays
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                metrics.incrementComparisons();
                arr[j + 1] = arr[j];
                j--;
            }
            metrics.incrementComparisons();
            arr[j + 1] = key;
        }
        return arr[left + (right - left) / 2];
    }
    
    private int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;
        
        for (int j = left; j < right; j++) {
            metrics.incrementComparisons();
            if (arr[j] <= pivot) {
                i++;
                ArrayUtils.swap(arr, i, j);
            }
        }
        
        ArrayUtils.swap(arr, i + 1, right);
        return i + 1;
    }
    
    private int findIndex(int[] arr, int left, int right, int value) {
        for (int i = left; i <= right; i++) {
            if (arr[i] == value) return i;
        }
        return -1;
    }
}