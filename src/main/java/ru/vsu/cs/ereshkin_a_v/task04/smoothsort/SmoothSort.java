package ru.vsu.cs.ereshkin_a_v.task04.smoothsort;

import java.util.Comparator;
import java.util.function.Consumer;

import static ru.vsu.cs.ereshkin_a_v.task04.smoothsort.SortUtils.getLeonardoNumbers;

@SuppressWarnings("unused")
public final class SmoothSort {

	private static final int LEONARDO_NUMBERS_LEN;
	private static final int[] LEONARDO_NUMS;

	static {
		// Предзагружаем числа Леонардо и их длину в константы для удобства.
		LEONARDO_NUMS = getLeonardoNumbers();
		LEONARDO_NUMBERS_LEN = LEONARDO_NUMS.length;
	}

	private SmoothSort() {
	}

	public static <T extends Comparable<T>> void sort(T[] data) {
		sort(data, Comparator.naturalOrder());
	}

	public static <T> void sort(T[] data, Comparator<T> comparator) {
		LeonardoHeap heap = makeHeap(data, comparator);
		for (int i = data.length - 1; i > 0; i--) {
			removeSmallestRoot(data, comparator, i, heap);
		}
	}

	private static <T> void siftDown(T[] array, Comparator<T> comparator, int rootPosition, T root,
	                                 int treeOrder) {
		// Погружаем корневой узел вниз по дереву,
		// пока не дойдём до дерева без дочерних узлов (дерево порядка 0 или 1)
		while (treeOrder > 1) {
			// Позиция элемента массива, отвечающего за конкретный дочерний элемент Леонардовы кучи.
			// В случае с правым элементом всё банально - тупо на одну позицию левее корня.
			int rightChildPosition = rootPosition - 1;
			// Дочерний элемент "справа"
			T rightChild = array[rightChildPosition];
			// right child's order is root's order - 2 (left child is root's order - 1)
			int leftChildPosition = rootPosition - 1 - LEONARDO_NUMS[treeOrder - 2];
			T leftChild = array[leftChildPosition];
			T child;
			int childPosition, childOrder;
			if (comparator.compare(leftChild, rightChild) > 0) {
				child = leftChild;
				childPosition = leftChildPosition;
				childOrder = treeOrder - 1;
			} else {
				child = rightChild;
				childPosition = rightChildPosition;
				childOrder = treeOrder - 2;
			}
			if (comparator.compare(root, child) >= 0) {
				// root is not smaller, no need to sift down any further
				return;
			}
			// sift root down to child and then examine that subtree
			array[childPosition] = root;
			array[rootPosition] = child;
			rootPosition = childPosition;
			treeOrder = childOrder;
		}
	}

	private static <T> void repairRoots(T[] array, Comparator<T> comparator,
	                                    int last, long structureTrees, int structureOffset) {
		int rootPosition = last;
		T currentRoot = array[rootPosition];
		while (rootPosition >= LEONARDO_NUMS[structureOffset]) {
			T largest;
			int largestPosition;
			int largestOrder;
			if (structureOffset > 1) {
				// this root is not order 0 or 1, so it has two children - make sure we're
				// comparing against the largest out of the root or its two immediate children
				int rightPosition = rootPosition - 1;
				T rightChild = array[rightPosition];
				// right child's order is root's order - 2 (left child is root's order - 1)
				int leftPosition = rightPosition - LEONARDO_NUMS[structureOffset - 2];
				T leftChild = array[leftPosition];
				if (comparator.compare(leftChild, rightChild) > 0) {
					if (comparator.compare(leftChild, currentRoot) > 0) {
						largest = leftChild;
						largestPosition = leftPosition;
						largestOrder = structureOffset - 1;
					} else {
						largest = currentRoot;
						largestPosition = rootPosition;
						largestOrder = structureOffset;
					}
				} else if (comparator.compare(rightChild, currentRoot) > 0) {
					largest = rightChild;
					largestPosition = rightPosition;
					largestOrder = structureOffset - 2;
				} else {
					largest = currentRoot;
					largestPosition = rootPosition;
					largestOrder = structureOffset;
				}
			} else {
				largest = currentRoot;
				largestPosition = rootPosition;
				largestOrder = structureOffset;
			}

			int previousRootPos = rootPosition - LEONARDO_NUMS[structureOffset];
			T previousRoot = array[previousRootPos];
			if (comparator.compare(largest, previousRoot) >= 0) {
				// previous root is not bigger, so we're done
				if (largest != currentRoot) {
					// if the root is not right, swap and then sift-down
					array[rootPosition] = largest;
					array[largestPosition] = currentRoot;
					siftDown(array, comparator, largestPosition, currentRoot, largestOrder);
				}
				return;
			}
			// Меняем корни деревьев
			array[rootPosition] = previousRoot;
			array[previousRootPos] = currentRoot;

			// Находим предыдущее дерево.
			rootPosition = previousRootPos;
			structureTrees &= ~1;
			int shift = Long.numberOfTrailingZeros(structureTrees);
			structureTrees >>= shift;
			structureOffset += shift;
		}

		// shuffle the root down if necessary to maintain heap invariant for this tree
		siftDown(array, comparator, rootPosition, currentRoot, structureOffset);
	}

	private static <T> void addToHeap(T[] array, Comparator<T> comp, int last,
	                                  LeonardoHeap heap) {
		heap.addTree();
		repairAfterAdd(array, comp, last, heap);
	}

	private static <T> void repairAfterAdd(T[] array, Comparator<T> comp,
	                                       int last, LeonardoHeap heap) {
		int len = array.length;
		switch (heap.offset) {
			case 0 -> {
				// if the last heap has order 0, we only rectify if it's the very last element in list
				if (last == len - 1) {
					repairRoots(array, comp, last, heap.trees, heap.offset);
				}
			}
			case 1 -> {
				// if the last heap has order 1, we can rectify if it's the last element in the list or
				// if it's the next-to-last element in list and won't be merged when we addTree last element
				if (last == len - 1 || (last == len - 2 && (heap.trees & 2) == 0)) {
					repairRoots(array, comp, last, heap.trees, heap.offset);
				}
			}
			default -> {
				// otherwise, if there is insufficient room in the list for this tree to be merged
				// into next order tree, then we can rectify
				int elementsRemaining = len - 1 - last;
				int elementsNeededForNextOrderTree = LEONARDO_NUMS[heap.offset - 1] + 1;
				if (elementsRemaining < elementsNeededForNextOrderTree) {
					repairRoots(array, comp, last, heap.trees, heap.offset);
				} else {
					siftDown(array, comp, last, array[last], heap.offset);
				}
			}
		}
	}

	private static <T> LeonardoHeap makeHeap(T[] array, Comparator<T> comp) {
		LeonardoHeap structure = new LeonardoHeap();
		for (int i = 0, len = array.length; i < len; i++) {
			addToHeap(array, comp, i, structure);
		}
		return structure;
	}

	private static <T> void removeSmallestRoot(T[] array, Comparator<T> comp, int last,
	                                           LeonardoHeap structure) {
		// smallest heap is order 0 or 1, can just drop it
		if (structure.offset <= 1) {
			structure.trees = structure.trees & ~1;
			if (structure.trees != 0) {
				int shift = Long.numberOfTrailingZeros(structure.trees);
				structure.trees = structure.trees >> shift;
				structure.offset += shift;
			}
			return;
		}
		// Разбиваем наименьшую кучу на две более мелких путём соответствия:
		// { tree1, offset } -> { tree011, offset - 2}
		structure.subdivideIntoTwoSmaller();
		// grab the two immediate children of smallest heap, which are the
		// roots of its two smaller order heaps.
		// last element is the root of the smallest tree; next one over is its right child
		int rightRoot = last - 1;
		// right child's order is root's order - 2 (left child is root's order - 1)
		// we've already subtracted 2 from root's order
		int leftRoot = rightRoot - LEONARDO_NUMS[structure.offset];
		// repair roots up to the left child
		repairRoots(array, comp, leftRoot, structure.trees >> 1, structure.offset + 1);
		// and then including the right child
		repairRoots(array, comp, rightRoot, structure.trees, structure.offset);
	}
}
