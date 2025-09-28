package com.dnc.algorithms;

import java.util.Arrays;
import java.util.Random;

import com.dnc.algorithms.geometry.ClosestPair;
import com.dnc.algorithms.metrics.MetricsTracker;
import com.dnc.algorithms.select.DeterministicSelect;
import com.dnc.algorithms.sort.MergeSort;
import com.dnc.algorithms.sort.QuickSort;

public class CLI {
    private static final Random RANDOM = new Random();
    private final TestRunner testRunner;
    
    public CLI() {
        this.testRunner = new TestRunner();
    }
    
    public void execute(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }
        
        String command = args[0];
        switch (command) {
            case "test":
                testRunner.runAllTests();
                break;
            case "bench":
                runBenchmarks();
                break;
            case "demo":
                runDemo();
                break;
            case "help":
                printHelp();
                break;
            default:
                System.out.println("Unknown command. Use 'help' for usage.");
                break;
        }
    }
    
    private void runBenchmarks() {
    System.out.println("Running Performance Benchmarks\n");
        
        int[] sizes = {1000, 5000, 10000, 50000};
        for (int size : sizes) {
            System.out.printf("Array Size: %,d%n", size);
            benchmarkSorting(size);
            benchmarkSelection(size);
            if (size <= 10000) {
                benchmarkClosestPair(size / 10);
            }
            System.out.println();
        }
    }
    
    private void benchmarkSorting(int size) {
        int[] data = generateRandomArray(size);
        
        // Benchmark MergeSort
        long start = System.nanoTime();
        new MergeSort(new MetricsTracker()).sort(Arrays.copyOf(data, data.length));
        long mergeTime = System.nanoTime() - start;
        
        // Benchmark QuickSort
        start = System.nanoTime();
        new QuickSort(new MetricsTracker()).sort(Arrays.copyOf(data, data.length));
        long quickTime = System.nanoTime() - start;
        
        System.out.printf("  MergeSort: %6.2f ms | QuickSort: %6.2f ms%n", 
            mergeTime / 1_000_000.0, quickTime / 1_000_000.0);
    }
    
    private void benchmarkSelection(int size) {
        int[] data = generateRandomArray(size);
        int k = size / 2;
        
        long start = System.nanoTime();
        new DeterministicSelect(new MetricsTracker()).select(Arrays.copyOf(data, data.length), k);
        long time = System.nanoTime() - start;
        
    System.out.printf("  Select k=%d: %6.2f ms%n", k, time / 1_000_000.0);
    }
    
    private void benchmarkClosestPair(int size) {
        ClosestPair.Point[] points = generateRandomPoints(size);
        
        long start = System.nanoTime();
        new ClosestPair(new MetricsTracker()).findClosestDistance(points);
        long time = System.nanoTime() - start;
        
    System.out.printf("  ClosestPair (n=%d): %6.2f ms%n", size, time / 1_000_000.0);
    }
    
    private void runDemo() {
    System.out.println("Algorithm Demonstration\n");
        
        // Sorting Demo
    System.out.println("1. Sorting Algorithms:");
        int[] numbers = {64, 34, 25, 12, 22, 11, 90, 5, 77, 42};
        System.out.println("   Input:  " + Arrays.toString(numbers));
        
        int[] mergeCopy = Arrays.copyOf(numbers, numbers.length);
        new MergeSort(new MetricsTracker()).sort(mergeCopy);
        System.out.println("   MergeSort: " + Arrays.toString(mergeCopy));
        
        int[] quickCopy = Arrays.copyOf(numbers, numbers.length);
        new QuickSort(new MetricsTracker()).sort(quickCopy);
        System.out.println("   QuickSort: " + Arrays.toString(quickCopy));
        
        // Selection Demo
        System.out.println("\n2. Selection Algorithm:");
        int[] selectArray = {7, 10, 4, 3, 20, 15, 8, 12, 6};
        for (int k = 1; k <= 3; k++) {
            int result = new DeterministicSelect(new MetricsTracker())
                .select(Arrays.copyOf(selectArray, selectArray.length), k);
            System.out.printf("   %d-smallest: %d%n", k, result);
        }
        
        // Closest Pair Demo
        System.out.println("\n3. Closest Pair Algorithm:");
        ClosestPair.Point[] points = {
            new ClosestPair.Point(1, 2),
            new ClosestPair.Point(4, 6), 
            new ClosestPair.Point(2, 3),
            new ClosestPair.Point(5, 1)
        };
        double distance = new ClosestPair(new MetricsTracker()).findClosestDistance(points);
        System.out.printf("   Closest distance: %.3f%n", distance);
    }
    
    private void printHelp() {
        System.out.println(
            "Divide & Conquer Algorithms CLI\n" +
            "=================================\n" +
            "\n" +
            "Commands:\n" +
            "  test    - Run comprehensive correctness tests\n" +
            "  bench   - Run performance benchmarks with various sizes\n" +
            "  demo    - See algorithms in action with sample data\n" +
            "  help    - Show this help message\n" +
            "\n" +
            "Examples:\n" +
            "  mvn exec:java -Dexec.args=\"test\"\n" +
            "  mvn exec:java -Dexec.args=\"bench\"\n" +
            "  mvn exec:java -Dexec.args=\"demo\"\n"
        );
    }
    
    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RANDOM.nextInt(size * 10);
        }
        return arr;
    }
    
    private ClosestPair.Point[] generateRandomPoints(int count) {
        ClosestPair.Point[] points = new ClosestPair.Point[count];
        for (int i = 0; i < count; i++) {
            points[i] = new ClosestPair.Point(RANDOM.nextDouble() * 100, RANDOM.nextDouble() * 100);
        }
        return points;
    }
}