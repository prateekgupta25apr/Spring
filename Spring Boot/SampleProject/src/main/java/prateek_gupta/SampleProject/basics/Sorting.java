package prateek_gupta.SampleProject.basics;

import java.util.Arrays;

public class Sorting {
    /**
     * In this sorting the first element is compared against all the elements then 2nd elements
     * is compared all elements from 2nd to last element, then 3rd element is
     * compared all elements from 3rd to last element and so on.
     * <br>
     * Time Complexity : O(n^2)
     */
    static int[] bubbleSort(int[] a){
        int temp=0;
        for(int i=0;i<(a.length-1);i++){
            for(int j=0;j<(a.length-i-1);j++){
                // For ascending order
                if(a[j]>a[j+1])
                    temp = a[j];
                a[j]=a[j+1];
                a[j+1]=temp;

                // For descending order
//                if(a[j]>a[j+1])
//                    temp = a[j];
//                a[j]=a[j+1];
//                a[j+1]=temp;
            }
        }

        return a;
    }

    /**
     * In this sorting we search for the smallest element in each iteration and put it at the
     * correct place.
     * <br>
     * Time Complexity : O(n^2)
     */
    public static void selSort(int[] arr) {
        int size = arr.length;
        int small, pos, temp;

        for (int i = 0; i < size - 1; i++) {
            small = arr[i];
            pos = i;

            // Notice initialization of j
            for (int j = i + 1; j < size; j++) {
                if (arr[j] < small) {
                    small = arr[j];
                    pos = j;
                }
            }

            // Swap
            temp = arr[i];
            arr[i] = arr[pos];
            arr[pos] = temp;

            System.out.println("\nArray after pass " + (i + 1) + " is:");
            for (int k : arr) {
                System.out.print(k + " ");
            }
        }
    }


    /**
     * The recursive call to the mergeSort() method reaches a point where only two adjacent
     * elements are compared in the merge() method. As recursion unwinds, more and more
     * elements are compared.
     * So basically the idea is to compare first 2 elements, then 4 elements,etc. and so on
     * also if the number of elements are not even then the right sub array gets less elements
     * <br>
     * time complexity of O(n log n)
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
     * In this first we create 2 arrays from the array arr, as
     * 1st array : from position left to mid
     * 2nd array : from position mid-1 to right
     * then we loop through both the arrays and compare the values and update the array arr
     * <br>
     * As its more of linear search and update hence the time complexity is linear
     * Time Complexity : O(n)
     */
    static void merge(int[] arr, int left, int mid, int right) {
        int leftArraySize = mid - left + 1;
        int rightArraySize = right - mid;
        int[] leftArray = new int[leftArraySize];
        int[] rightArray = new int[rightArraySize];

        // Preparing leftArray
        System.arraycopy(arr, left, leftArray, 0, leftArraySize);

        // Preparing RightArray
        System.arraycopy(arr, (mid + 1), rightArray, 0, rightArraySize);

        int leftArrayPointer = 0, rightArrayPointer = 0, mainArrayPointer = left;

        // Comparing values in both the array and update the array arr
        while (leftArrayPointer < leftArraySize && rightArrayPointer < rightArraySize) {
            if (leftArray[leftArrayPointer] <= rightArray[rightArrayPointer]) {
                arr[mainArrayPointer] = leftArray[leftArrayPointer];
                leftArrayPointer++;
            } else {
                arr[mainArrayPointer] = rightArray[rightArrayPointer];
                rightArrayPointer++;
            }
            mainArrayPointer++;
        }

        // Updating array arr with the left-over values in left array
        while (leftArrayPointer < leftArraySize) {
            arr[mainArrayPointer] = leftArray[leftArrayPointer];
            leftArrayPointer++;
            mainArrayPointer++;
        }

        // Updating array arr with the left-over values in right array
        while (rightArrayPointer < rightArraySize) {
            arr[mainArrayPointer] = rightArray[rightArrayPointer];
            rightArrayPointer++;
            mainArrayPointer++;
        }
    }

    public static void main(String[] args) {
        int[] data = {64, 25, 12, 22, 11};
        System.out.println(Arrays.toString(bubbleSort(data)));

        int[] array = {12, 11, 13, 1, 6, 5, 7};
        long startTime = System.nanoTime();
        recursiveMergeSort(array, 0, array.length - 1);
//        iterativeMergeSort(array);
        System.out.println("time " + (System.nanoTime() - startTime) + "ms");
        System.out.println(Arrays.toString(array));
    }
}
