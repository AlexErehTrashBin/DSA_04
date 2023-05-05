package ru.vsu.cs.ereshkin_a_v.task04.tests

import org.junit.jupiter.api.Test
import ru.vsu.cs.ereshkin_a_v.task04.smoothsort.SmoothSort
import ru.vsu.cs.ereshkin_a_v.task04.util.SortStatus
import ru.vsu.cs.ereshkin_a_v.task04.util.SortStatusType
import ru.vsu.cs.util.ArrayUtils

class SortIterationsTests {
	@Test
	fun test1(){
		val intArray: IntArray = intArrayOf(36, 22, 97, 21, 37, 81, 41, 60, 52, 9, 29, 75)
		println("Проверка на массиве: ${ArrayUtils.toString(intArray)}")
		val typedArray = intArray.toTypedArray()
		val sortStates = mutableListOf<SortStatus<Int>>()
		SmoothSort.sort(typedArray) { sortStatus ->
			//println("")
			sortStates.add(sortStatus)
		}
		println("Отсортированный массив: ${ArrayUtils.toString(typedArray.toIntArray())}")
		sortStates.forEachIndexed { index, sortStatus ->
			if (sortStatus.type == SortStatusType.START_FIX_AFTER_ADD) return@forEachIndexed
			val statusMessage: String = when(sortStatus.type) {
				SortStatusType.ELEMENTS_SWAPPED -> "элементы поменялись местами"
				SortStatusType.HEAP_BUILT -> "построена куча"
				SortStatusType.ELEMENT_SIFTED_DOWN -> "был погружен элемент"
				SortStatusType.TREES_REPAIRED -> "восстановлено дерево"
				SortStatusType.ELEMENT_REMOVED -> "удалён наименьший корень"
				SortStatusType.START_FIX_AFTER_ADD -> "восстанавливаем дерево после добавления элемента в кучу"
				else -> ""
			}
			println("Состояние №$index:  ${ArrayUtils.toString(sortStatus.array.toIntArray())}  ($statusMessage)")
		}
	}
}