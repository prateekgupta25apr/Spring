package prateek_gupta.SampleProject.basics.general;

/**
 * Question:
 * Given an unsorted array of integer from 1 to n, swap the values in the array to
 * sort the array with min number of swaps
 * <br>
 * Imp Trick
 * Concept of cycles in Swapping
 */
public class SwapsAndCycles {
    /**(WRONG)
     * The basic logic of this approach is count the number records not matching its position
     * and calculate it and return as minimum swaps.
     */
    public static int approach1(int[] arr) {
        long startTime = System.currentTimeMillis();
        int minimumSwaps = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != (i + 1))
                minimumSwaps++;
        }
        System.out.println("Approach 1: " + (System.currentTimeMillis() - startTime) + "ms");
        return minimumSwaps;
    }

    /**(CORRECT)
     * The basic logic of cycles is that a cycle comprises elements that can be swapped among
     * themselves to place each in its correct position. For example, if element a should go
     * to the position of b, b to c, c to d, and d back to a, then a -> b -> c -> d -> a forms
     * a cycle. The total number of swaps required is the sum of swaps for each cycle, where a
     * cycle of length n requires n swaps.
     */
    public static int approach2(int[] arr) {
        long startTime = System.currentTimeMillis();
        int minimumSwaps = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == (i + 1))
                continue;

            int currentCycleSwaps = 0;


            while (arr[i] != (i + 1)) {
                int j= arr[i] - 1;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                currentCycleSwaps++;
            }

            if (currentCycleSwaps > 0)
                minimumSwaps += currentCycleSwaps;
        }
        System.out.println("Approach 2: " + (System.currentTimeMillis() - startTime) + "ms");
        return minimumSwaps;
    }

    /**(CORRECT)
     * The basic logic of cycles is that a cycle comprises elements that can be swapped among
     * themselves to place each in its correct position. For example, if element a should go
     * to the position of b, b to c, c to d, and d back to a, then a -> b -> c -> d -> a forms
     * a cycle. The total number of swaps required is the sum of swaps for each cycle, where a
     * cycle of length n requires n swaps.
     * <br>
     * Instead of actually swapping the elements in the array, a boolean array is used to
     * track visited elements, marking them as true once they are considered, ensuring
     * elements already in the correct position or previously visited are skipped.
     * This approach efficiently calculates the minimum number of swaps needed to
     * sort the array but as a swap need 2 elements but in this approach we just validate
     * elements 1 by 1 so the formula becomes
     * swaps = number of wrongly positioned elements - 1
     */
    public static int approach3(int[] arr) {
        long startTime = System.currentTimeMillis();
        // Creating an array to keep track of values we have visited
        boolean[] swapped = new boolean[arr.length];

        int minimumSwaps = 0;

        for (int i = 0; i < arr.length; i++) {
            if (swapped[i] || arr[i] == (i + 1))
                continue;

            int currentCycleSwaps = 0;
            int j = i;

            // Updating all the corresponding values in swapped array for which to be
            // updated in 1 cycle
            while (!swapped[j]) {
                swapped[j] = true;
                j = arr[j] - 1;
                currentCycleSwaps++;
            }

            if (currentCycleSwaps > 0)
                minimumSwaps += currentCycleSwaps - 1;
        }
        System.out.println("Approach 3: " + (System.currentTimeMillis() - startTime) + "ms");
        return minimumSwaps;
    }

    public static void main(String[] args) {
        int[] arr = {
                2, 31, 1, 38, 29, 5, 44, 6, 12, 18, 39, 9, 48, 49, 13, 11, 7, 27, 14, 33, 50,
                21, 46, 23, 15, 26, 8, 47, 40, 3, 32, 22, 34, 42, 16, 41, 24, 10, 4, 28, 36,
                30, 37, 35, 20, 17, 45, 43, 25, 19};
        System.out.println("Minimum swaps needed (Approach 1): " + approach1(arr));

        arr = new int[]{
                2, 31, 1, 38, 29, 5, 44, 6, 12, 18, 39, 9, 48, 49, 13, 11, 7, 27, 14, 33, 50,
                21, 46, 23, 15, 26, 8, 47, 40, 3, 32, 22, 34, 42, 16, 41, 24, 10, 4, 28, 36,
                30, 37, 35, 20, 17, 45, 43, 25, 19};
        System.out.println("Minimum swaps needed (Approach 2): " + approach2(arr));

        arr = new int[]{
                2, 31, 1, 38, 29, 5, 44, 6, 12, 18, 39, 9, 48, 49, 13, 11, 7, 27, 14, 33, 50,
                21, 46, 23, 15, 26, 8, 47, 40, 3, 32, 22, 34, 42, 16, 41, 24, 10, 4, 28, 36,
                30, 37, 35, 20, 17, 45, 43, 25, 19};
        System.out.println("Minimum swaps needed (Approach 3): " + approach3(arr));
    }
}
