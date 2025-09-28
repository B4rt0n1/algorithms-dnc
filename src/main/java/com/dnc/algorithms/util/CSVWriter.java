package com.dnc.algorithms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter implements AutoCloseable {
    private final BufferedWriter writer;

    public CSVWriter(String filePath, boolean append) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        writer = new BufferedWriter(new FileWriter(file, append));
    }

    public void writeHeader() throws IOException {
        writer.write("algorithm,size,run,time_ms,max_recursion_depth,comparisons,copies\n");
    }

    public void writeRow(String algorithm, int size, int run, long timeMs,
                         int maxDepth, long comparisons, long copies) throws IOException {
        writer.write(String.format("%s,%d,%d,%d,%d,%d,%d\n",
                algorithm, size, run, timeMs, maxDepth, comparisons, copies));
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}