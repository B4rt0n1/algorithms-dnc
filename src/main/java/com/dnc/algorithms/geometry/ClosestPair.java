package com.dnc.algorithms.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnc.algorithms.metrics.MetricsTracker;

public class ClosestPair {
    private final MetricsTracker metrics;
    
    public ClosestPair(MetricsTracker metrics) {
        this.metrics = metrics;
    }
    
    public static class Point {
        public final double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public double distanceTo(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    
    public double findClosestDistance(Point[] points) {
        Point[] pointsByX = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsByX, (a, b) -> Double.compare(a.x, b.x));
        
        Point[] pointsByY = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsByY, (a, b) -> Double.compare(a.y, b.y));
        
        metrics.incrementAllocations(points.length * 2);
        return findClosestDistance(pointsByX, pointsByY, 0, points.length - 1);
    }
    
    private double findClosestDistance(Point[] pointsByX, Point[] pointsByY, int left, int right) {
        metrics.enterRecursion();
        
        int n = right - left + 1;
        if (n <= 3) {
            metrics.exitRecursion();
            return bruteForce(pointsByX, left, right);
        }
        
        int mid = left + (right - left) / 2;
        Point midPoint = pointsByX[mid];
        
        // Split pointsByY into left and right halves
        Point[] leftPointsY = new Point[mid - left + 1];
        Point[] rightPointsY = new Point[right - mid];
        int leftIdx = 0, rightIdx = 0;
        
        for (Point p : pointsByY) {
            if (p.x <= midPoint.x) {
                leftPointsY[leftIdx++] = p;
            } else {
                rightPointsY[rightIdx++] = p;
            }
        }
        
        metrics.incrementAllocations(leftPointsY.length + rightPointsY.length);
        
        double leftMin = findClosestDistance(pointsByX, leftPointsY, left, mid);
        double rightMin = findClosestDistance(pointsByX, rightPointsY, mid + 1, right);
        double minDistance = Math.min(leftMin, rightMin);
        
        // Check strip
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsByY) {
            if (Math.abs(p.x - midPoint.x) < minDistance) {
                strip.add(p);
            }
        }
        
        double stripMin = checkStrip(strip, minDistance);
        minDistance = Math.min(minDistance, stripMin);
        
        metrics.exitRecursion();
        return minDistance;
    }
    
    private double bruteForce(Point[] points, int left, int right) {
        double minDistance = Double.MAX_VALUE;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double dist = points[i].distanceTo(points[j]);
                minDistance = Math.min(minDistance, dist);
            }
        }
        return minDistance;
    }
    
    private double checkStrip(List<Point> strip, double minDistance) {
        double min = minDistance;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < min; j++) {
                double dist = strip.get(i).distanceTo(strip.get(j));
                min = Math.min(min, dist);
            }
        }
        return min;
    }
}