package ru.vsu.cs.ereshkin_a_v.task04.smoothsort;

import java.util.ArrayList;

public class SortUtils {
	public static int[] getLeonardoNumbers(){
		ArrayList<Integer> lnums = new ArrayList<>();
		int lp2 = 1;
		int lp1 = 1;
		lnums.add(lp1);
		lnums.add(lp2);
		while (true) {
			long lnext = (long) lp2 + lp1 + 1;
			if (lnext > Integer.MAX_VALUE) {
				// done
				break;
			}
			int lp0 = (int) lnext;
			lnums.add(lp0);
			lp2 = lp1;
			lp1 = lp0;
		}
		int[] result = new int[lnums.size()];
		for (int i = 0; i < lnums.size(); i++) {
			result[i] = lnums.get(i);
		}
		return result;
	}
}
