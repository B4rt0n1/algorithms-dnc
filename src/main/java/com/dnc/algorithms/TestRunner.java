package com.dnc.algorithms;

import java.util.Arrays;
import java.util.Random;

import com.dnc.algorithms.geometry.ClosestPair;
import com.dnc.algorithms.metrics.MetricsTracker;
import com.dnc.algorithms.select.DeterministicSelect;
import com.dnc.algorithms.sort.MergeSort;
import com.dnc.algorithms.sort.QuickSort;
import com.dnc.algorithms.util.ArrayUtils;

/**
 * Minimal test runner used by the CLI to perform quick correctness checks.
 */
public class TestRunner {
    private final Random random = new Random(42);

    public void runAllTests() {
        System.out.println("Running quick correctness tests\n");
        int failures = 0;

        if (!testSorting()) failures++;
        if (!testSelection()) failures++;
        if (!testClosestPair()) failures++;

        System.out.printf("\nSummary: %d failure(s)\n", failures);
        if (failures == 0) System.out.println("All quick tests passed.");
    }

    private boolean testSorting() {
        System.out.print("[sorting] ");
        int[] arr = generateRandomArray(1000);
        int[] a1 = Arrays.copyOf(arr, arr.length);
        int[] a2 = Arrays.copyOf(arr, arr.length);

        new MergeSort(new MetricsTracker()).sort(a1);
        new QuickSort(new MetricsTracker()).sort(a2);

        boolean ok = ArrayUtils.isSorted(a1) && ArrayUtils.isSorted(a2);
        System.out.println(ok ? "OK" : "FAILED");
        return ok;
    }

    private boolean testSelection() {
        System.out.print("[select]  ");
        int[] arr = generateRandomArray(201);
        int[] sorted = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sorted);

        DeterministicSelect selector = new DeterministicSelect(new MetricsTracker());
        // check first 5 order-statistics
        for (int k = 1; k <= 5; k++) {
            int expected = sorted[k - 1];
            int got = selector.select(Arrays.copyOf(arr, arr.length), k);
            if (got != expected) {
                System.out.println("FAILED (k=" + k + ")");
                return false;
            }
        }

        System.out.println("OK");
        return true;
    }

    private boolean testClosestPair() {
        System.out.print("[closest] ");
        ClosestPair.Point[] pts = new ClosestPair.Point[] {
            new ClosestPair.Point(0, 0),
            new ClosestPair.Point(1, 0),
            new ClosestPair.Point(0, 2),
            new ClosestPair.Point(2, 2)
        };

        double found = new ClosestPair(new MetricsTracker()).findClosestDistance(pts);
        double expected = 1.0; // point (0,0) and (1,0)
        boolean ok = Math.abs(found - expected) < 1e-6;
        System.out.println(ok ? "OK" : ("FAILED (got=" + found + ")"));
        return ok;
    }

    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = random.nextInt(size * 10 + 1);
        return arr;
    }
}
