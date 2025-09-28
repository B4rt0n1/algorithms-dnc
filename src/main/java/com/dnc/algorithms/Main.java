package com.dnc.algorithms;

import com.dnc.algorithms.metrics.MetricsTracker;
import com.dnc.algorithms.sort.MergeSort;
import com.dnc.algorithms.sort.QuickSort;
import com.dnc.algorithms.select.DeterministicSelect;
import com.dnc.algorithms.geometry.ClosestPair;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();
    
    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("help")) {
            printHelp();
            return;
        }

        String command = args[0];
        switch (command) {
            case "test" -> runTests();
            case "bench" -> runBenchmarks();
            case "demo" -> runDemo();
            default -> printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("""
            Divide & Conquer Algorithms
            Commands:
              test    - Run correctness tests
              bench   - Run performance benchmarks  
              demo    - Run algorithm demonstration
            """);
    }

    private static void runTests() {
        System.out.println("Running Algorithm Tests...");
        
        // Test MergeSort
        testSortAlgorithm("MergeSort", new MergeSort(new MetricsTracker()));
        
        // Test QuickSort  
        testSortAlgorithm("QuickSort", new QuickSort(new MetricsTracker()));
        
        // Test Deterministic Select
        testSelectAlgorithm();
        
        // Test Closest Pair
        testClosestPairAlgorithm();
        
        System.out.println("✅ All tests completed!");
    }

    private static void testSortAlgorithm(String name, Object sorter) {
        System.out.printf("Testing %s... ", name);
        int[] testArray = generateRandomArray(100);
        int[] expected = Arrays.copyOf(testArray, testArray.length);
        Arrays.sort(expected);
        
        if (sorter instanceof MergeSort) {
            ((MergeSort) sorter).sort(testArray);
        } else if (sorter instanceof QuickSort) {
            ((QuickSort) sorter).sort(testArray);
        }
        
        if (Arrays.equals(testArray, expected)) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }

    private static void testSelectAlgorithm() {
        System.out.print("Testing DeterministicSelect... ");
        int[] arr = {7, 10, 4, 3, 20, 15};
        DeterministicSelect selector = new DeterministicSelect(new MetricsTracker());
        int result = selector.select(Arrays.copyOf(arr, arr.length), 3);
        
        if (result == 7) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }

    private static void testClosestPairAlgorithm() {
        System.out.print("Testing ClosestPair... ");
        ClosestPair closest = new ClosestPair(new MetricsTracker());
        ClosestPair.Point[] points = {
            new ClosestPair.Point(0, 0),
            new ClosestPair.Point(1, 1),
            new ClosestPair.Point(3, 3)
        };
        
        double result = closest.findClosestDistance(points);
        if (Math.abs(result - Math.sqrt(2)) < 0.001) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }

    private static void runBenchmarks() {
        System.out.println("Running Benchmarks...");
        
        int[] sizes = {1000, 5000, 10000};
        for (int size : sizes) {
            System.out.printf("\nSize: %,d\n", size);
            benchmarkAlgorithm("MergeSort", new MergeSort(new MetricsTracker()), size);
            benchmarkAlgorithm("QuickSort", new QuickSort(new MetricsTracker()), size);
            benchmarkSelect(size);
            benchmarkClosestPair(size / 10);
        }
    }

    private static void benchmarkAlgorithm(String name, Object algorithm, int size) {
        int[] data = generateRandomArray(size);
        long startTime = System.nanoTime();
        
        if (algorithm instanceof MergeSort) {
            ((MergeSort) algorithm).sort(data);
        } else if (algorithm instanceof QuickSort) {
            ((QuickSort) algorithm).sort(data);
        }
        
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-12s: %.3f ms\n", name, timeMs);
    }

    private static void benchmarkSelect(int size) {
        int[] data = generateRandomArray(size);
        DeterministicSelect selector = new DeterministicSelect(new MetricsTracker());
        int k = size / 2;
        
        long startTime = System.nanoTime();
        selector.select(data, k);
        long endTime = System.nanoTime();
        
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-12s: %.3f ms\n", "Select", timeMs);
    }

    private static void benchmarkClosestPair(int size) {
        ClosestPair.Point[] points = generateRandomPoints(size);
        ClosestPair closest = new ClosestPair(new MetricsTracker());
        
        long startTime = System.nanoTime();
        closest.findClosestDistance(points);
        long endTime = System.nanoTime();
        
        double timeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("  %-12s: %.3f ms\n", "ClosestPair", timeMs);
    }

    private static void runDemo() {
        System.out.println("Algorithm Demonstration\n");
        
        // Demo sorting
        int[] numbers = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Original: " + Arrays.toString(numbers));
        
        MergeSort sorter = new MergeSort(new MetricsTracker());
        sorter.sort(numbers);
        System.out.println("Sorted:   " + Arrays.toString(numbers));
        
        // Demo selection
        int[] selectArray = {7, 10, 4, 3, 20, 15};
        DeterministicSelect selector = new DeterministicSelect(new MetricsTracker());
        int third = selector.select(Arrays.copyOf(selectArray, selectArray.length), 3);
        System.out.printf("3rd smallest of %s: %d\n", Arrays.toString(selectArray), third);
    }

    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RANDOM.nextInt(size * 10);
        }
        return arr;
    }

    private static ClosestPair.Point[] generateRandomPoints(int count) {
        ClosestPair.Point[] points = new ClosestPair.Point[count];
        for (int i = 0; i < count; i++) {
            points[i] = new ClosestPair.Point(RANDOM.nextDouble() * 100, RANDOM.nextDouble() * 100);
        }
        return points;
    }
}