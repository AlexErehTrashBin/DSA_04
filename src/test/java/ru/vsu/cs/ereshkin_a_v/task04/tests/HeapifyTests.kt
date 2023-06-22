package ru.vsu.cs.ereshkin_a_v.task04.tests

import org.junit.jupiter.api.Test
import ru.vsu.cs.ereshkin_a_v.task04.heapsort.Utils.isArrayHeapified

class HeapifyTests {
	@Test
	fun test1() {
		val intArray = intArrayOf(5, 4, 3, 4, 6)
		println(isArrayHeapified(intArray))
	}
}