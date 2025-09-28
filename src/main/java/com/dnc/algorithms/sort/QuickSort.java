package com.dnc.algorithms.sort;

import com.dnc.algorithms.metrics.MetricsTracker;
import com.dnc.algorithms.util.ArrayUtils;

public class QuickSort {
    private final MetricsTracker metrics;
    
    public QuickSort(MetricsTracker metrics) {
        this.metrics = metrics;
    }
    
    public void sort(int[] arr) {
        ArrayUtils.shuffle(arr); // Randomize for probabilistic guarantee
        sort(arr, 0, arr.length - 1);
    }
    
    private void sort(int[] arr, int low, int high) {
        while (low < high) {
            metrics.enterRecursion();
            
            int pivot = partition(arr, low, high);
            
            // Recurse on smaller partition, iterate on larger
            if (pivot - low < high - pivot) {
                sort(arr, low, pivot - 1);
                low = pivot + 1;
            } else {
                sort(arr, pivot + 1, high);
                high = pivot - 1;
            }
            
            metrics.exitRecursion();
        }
    }
    
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            metrics.incrementComparisons();
            if (arr[j] <= pivot) {
                i++;
                ArrayUtils.swap(arr, i, j);
            }
        }
        
        ArrayUtils.swap(arr, i + 1, high);
        return i + 1;
    }
}