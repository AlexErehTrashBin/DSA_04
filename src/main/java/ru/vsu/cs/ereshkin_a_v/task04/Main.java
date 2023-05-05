package ru.vsu.cs.ereshkin_a_v.task04;

import ru.vsu.cs.ereshkin_a_v.task04.smoothsort.SmoothSort;
import ru.vsu.cs.util.ArrayUtils;

public class Main {
	public static void main(String[] args) {
		Integer[] ints = new Integer[]{7, 9, 2, 4, 8, 7, 2, 5, 9, 3};

		SmoothSort.sort(ints);
		System.out.println(ArrayUtils.toString(ArrayUtils.toPrimitive(ints)));
	}
}