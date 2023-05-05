package ru.vsu.cs.ereshkin_a_v.task04.smoothsort;

import static ru.vsu.cs.ereshkin_a_v.task04.smoothsort.BitUtils.isLowestBitZero;

class LeonardoHeap {
	/**
	 * Битовая маска, характеризующая порядок деревьев, которые представлены в куче.
	 */
	long trees;

	/**
	 * Порядок наименее значащего бита в маске деревьев (trees).
	 * Эффективно порядок наименьшего дерева в куче.
	 */
	int offset;

	LeonardoHeap() {
	}

	public void addTree() {
		if (isLowestBitZero(trees)) {
			// Младший бит битовой маски равен нулю -> куча пустая.
			// Поэтому добавляем Леонардово дерево порядка (размером равным) 1.
			trees |= 1;
			offset = 1;
		} else if ((trees & 3) == 3) {
			// Если последние два дерева в куче последовательные - мы можем их просто объединить
			// и новый элемент будет корневым элементом объединённого дерева.
			trees >>= 2; // shift away lowest two orders
			offset += 2;
			trees |= 1; // and replace with next highest order
		} else if (offset == 1) {
			// Текущее наименьшее дерево порядка 1 -> добавляем дерево порядка 0.
			trees <<= 1; // make room for new order of tree
			trees |= 1;
			offset = 0;
		} else {
			// Добавление нового дерева порядка 1
			trees <<= (offset - 1); // make room to push in the bit at order 1
			trees |= 1;
			offset = 1;
		}
	}

	public void subdivideIntoTwoSmaller() {
		// В маске деревьев оставляем все биты кроме самого первого
		trees &= ~1;
		// Сдвигаем получившуюся маску на 2 бита в лево
		trees <<= 2;
		// Устанавливаем 2 наименьших бита в 1
		trees |= 3;
		// Уменьшаем переменную смещения на 2
		offset -= 2;
	}
}
