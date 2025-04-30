package src;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] array = {12, 11, 13, 1, 6, 5, 7};
        //recursiveMergeSort(array, 0, array.length - 1);
        iterativeMergeSort(array);
        System.out.println(Arrays.toString(array));
    }

    /**
     * The recursive call to the mergeSort() method reaches a point where only two adjacent
     * elements are compared in the merge() method. As recursion unwinds, more and more
     * elements are compared. In the merge() method, we first create two sub-arrays, compare
     * their elements, and update the main array accordingly
     */
    static void recursiveMergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            recursiveMergeSort(array, left, mid);
            recursiveMergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    /**
     * In the iterative (bottom-up) approach of Merge Sort, the algorithm starts by comparing
     * adjacent elements, sorting sub-arrays of size 1 into sorted pairs of size 2.
     * Next, it merges pairs of 2 elements into sub-arrays of 4 elements, and if the array
     * size isn't evenly divisible, a smaller sub-array (like a pair of 2 elements with a
     * single element) is merged.
     * This process continues for sub-arrays of size 4, then 8, and so on, until the entire
     * array is sorted. At each step, the merge() method compares elements from two sub-arrays
     * and merges them into one sorted sub-array, ensuring the array remains sorted at every
     * level.
     * This approach maintains the overall time complexity of O(n log n), similar to the
     * recursive version.
     */
    static void iterativeMergeSort(int[] array){
        int n = array.length;
        for (int currSize = 1; currSize < n; currSize *= 2) {// Size Loop
            for (int leftStart = 0; leftStart < n - 1; leftStart += 2 * currSize) {// Elements Loop
                int mid = Math.min(leftStart + currSize - 1, n - 1);
                int rightEnd = Math.min(leftStart + (2 * currSize) - 1, n - 1);
                merge(array, leftStart, mid, rightEnd);
            }
        }
    }

    /**
     * Time Complexity : O(n)
     *
     * @param array is the array from which sub arrays will be created
     * @param left  is the starting range to create sub arrays
     * @param mid   is the mid of the range to create sub arrays
     * @param right is the ending range to create sub arrays
     */
    static void merge(int[] array, int left, int mid, int right) {
        int leftArraySize = mid - left + 1;
        int rightArraySize = right - mid;
        int[] leftArray = new int[leftArraySize];
        int[] rightArray = new int[rightArraySize];

        // Preparing leftArray
        System.arraycopy(array, left, leftArray, 0, leftArraySize);

        // Preparing RightArray
        System.arraycopy(array, (mid + 1), rightArray, 0, rightArraySize);

        int leftArrayPointer = 0, rightArrayPointer = 0, mainArrayPointer = left;

        while (leftArrayPointer < leftArraySize && rightArrayPointer < rightArraySize) {
            if (leftArray[leftArrayPointer] <= rightArray[rightArrayPointer]) {
                array[mainArrayPointer] = leftArray[leftArrayPointer];
                leftArrayPointer++;
            } else {
                array[mainArrayPointer] = rightArray[rightArrayPointer];
                rightArrayPointer++;
            }
            mainArrayPointer++;
        }

        while (leftArrayPointer < leftArraySize) {
            array[mainArrayPointer] = leftArray[leftArrayPointer];
            leftArrayPointer++;
            mainArrayPointer++;
        }

        while (rightArrayPointer < rightArraySize) {
            array[mainArrayPointer] = rightArray[rightArrayPointer];
            rightArrayPointer++;
            mainArrayPointer++;
        }
    }
}
