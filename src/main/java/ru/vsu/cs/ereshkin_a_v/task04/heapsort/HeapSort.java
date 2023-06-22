package ru.vsu.cs.ereshkin_a_v.task04.heapsort;

import java.util.Comparator;

public class HeapSort {
    private static <T> void siftDown(T[] data, int siftIndex, int to, int from, Comparator<T> c) {
        T value = data[siftIndex];
        while (true) {
            int childIndex = from + 2 * (siftIndex - from) + 1;
            if (childIndex >= to) {
                break;
            }
            if (childIndex + 1 < to && c.compare(data[childIndex + 1], data[childIndex]) > 0) {
                childIndex++;
            }
            if (c.compare(value, data[childIndex]) > 0) {
                break;
            }
            data[siftIndex] = data[childIndex];
            siftIndex = childIndex;
        }
        data[siftIndex] = value;
    }

    private static <T> void swap(T[] data, int i, int j) {
        T temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public static <T> void sort(T[] data, Comparator<T> c, int from, int to) {
        int heapSize = to - from;
        for (int i = (heapSize - 1) / 2; i >= 0; i--) {
            int siftIndex = from + i;
            int siftToBound = from + heapSize;
            siftDown(data, siftIndex, siftToBound, from, c);
        }
        while (heapSize > 1) {
            swap(data, from, from + heapSize - 1);
            heapSize--;
            siftDown(data, from, from + heapSize, from, c);
        }
    }

    public static <T extends Comparable<T>> void sort(T[] data, int from, int to) {
        sort(data, Comparable::compareTo, from, to);
    }
}