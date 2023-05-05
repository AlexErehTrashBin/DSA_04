package ru.vsu.cs.ereshkin_a_v.task04.smoothsort;

public class BitUtils {

	public static long getLowestBit(long number){
		return (number & 1);
	}
	public static boolean isLowestBitZero(long number){
		return getLowestBit(number) == 0;
	}
}
