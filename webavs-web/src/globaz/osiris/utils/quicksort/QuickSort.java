package globaz.osiris.utils.quicksort;

import java.util.List;

/**
 * @deprecated utiliser Collections.sort(list, Comparable);
 */
@Deprecated
public class QuickSort {
    private static int indexToCompare = 0;

    protected static int compare(ComparableVector a, ComparableVector b) {

        return a.compareTo(b, QuickSort.indexToCompare);

    }

    protected static int partition(List array, int start, int end) {
        int left, right;
        ComparableVector partitionElement;

        // Arbitrary partition start...there are better ways...
        partitionElement = (ComparableVector) array.get(end);

        left = start - 1;
        right = end;
        for (;;) {
            while (QuickSort.compare(partitionElement, (ComparableVector) array.get(++left)) > 0) {
                if (left == end) {
                    break;
                }
            }
            while (QuickSort.compare(partitionElement, (ComparableVector) array.get(--right)) < 0) {
                if (right == start) {
                    break;
                }
            }
            if (left >= right) {
                break;
            }
            QuickSort.swap(array, left, right);
        }
        QuickSort.swap(array, left, end);

        return left;
    }

    // Sorts entire array
    public static void sort(List array, int index) {
        QuickSort.indexToCompare = index;
        QuickSort.sort(array, 0, array.size() - 1);
    }

    // Sorts partial array
    public static void sort(List array, int start, int end) {
        int p;
        if (end > start) {
            p = QuickSort.partition(array, start, end);
            QuickSort.sort(array, start, p - 1);
            QuickSort.sort(array, p + 1, end);
        }
    }

    protected static void swap(List array, int i, int j) {
        Object temp;

        temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.07.2003 15:28:45)
     * 
     * @return int
     */
    public int getIndexToCompare() {
        return QuickSort.indexToCompare;
    }
}
