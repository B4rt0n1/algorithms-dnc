package com.dnc.algorithms.sort;

import com.dnc.algorithms.metrics.MetricsTracker;

public class MergeSort {
    private static final int CUTOFF = 15; // Switch to insertion sort for small arrays
    private final MetricsTracker metrics;
    
    public MergeSort(MetricsTracker metrics) {
        this.metrics = metrics;
    }
    
    public void sort(int[] arr) {
        int[] buffer = new int[arr.length];
        sort(arr, buffer, 0, arr.length - 1);
    }
    
    private void sort(int[] arr, int[] buffer, int left, int right) {
        metrics.enterRecursion();
        
        if (right - left <= CUTOFF) {
            insertionSort(arr, left, right);
            metrics.exitRecursion();
            return;
        }
        
        int mid = left + (right - left) / 2;
        sort(arr, buffer, left, mid);
        sort(arr, buffer, mid + 1, right);
        merge(arr, buffer, left, mid, right);
        
        metrics.exitRecursion();
    }
    
    private void merge(int[] arr, int[] buffer, int left, int mid, int right) {
        System.arraycopy(arr, left, buffer, left, right - left + 1);
        metrics.incrementAllocations(right - left + 1);
        
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            metrics.incrementComparisons();
            if (buffer[i] <= buffer[j]) {
                arr[k++] = buffer[i++];
            } else {
                arr[k++] = buffer[j++];
            }
        }
        
        while (i <= mid) arr[k++] = buffer[i++];
        while (j <= right) arr[k++] = buffer[j++];
    }
    
    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                metrics.incrementComparisons();
                arr[j + 1] = arr[j];
                j--;
            }
            metrics.incrementComparisons(); // Final comparison that fails
            arr[j + 1] = key;
        }
    }
}