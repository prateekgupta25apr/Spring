package src;

import java.util.*;

/**
 * <b>Question:</b>
 * Starting with a 1-indexed array of zeros and a list of operations, for each operation add
 * a value to each the array element between two given indices, inclusive. Once all operations
 * have been performed, return the maximum value in the array.
 * Example
 * n=10
 * queries=[[1,5,3],[4,8,7],[6,9,1]]
 * Queries are interpreted as follows:
 *     a b k
 *     1 5 3
 *     4 8 7
 *     6 9 1
 * Add the values of k between the indices a and b inclusive:
 * index->	 1 2 3  4  5 6 7 8 9 10
 * 	[0,0,0, 0, 0,0,0,0,0, 0] //1-indexed array of zeros
 * 	[3,3,3, 3, 3,0,0,0,0, 0] // added 3 for range 1 to 5
 * 	[3,3,3,10,10,7,7,7,0, 0] // added 7 for range 4 to 8
 * 	[3,3,3,10,10,8,8,8,1, 0] // added 1 for range 6 to 9
 * 	The largest value is 10 after all operations are performed.
 * 	<br>
 * 	<b>Solution:</b>
 * 	So basically we need to a number k for the range a to b inclusive so instead of adding the
 * 	k for the range a to b for all the queries we will just update the starting and the ending
 * 	of the range and then accumulate the sum.So
 * 	Increment Start: By adding k at index a-1(as provided index values start from 1 not 0),
 * 	we indicate that starting from this index, the values should be incremented by k.
 *  Increment End: By subtracting k at index b, we indicate that the increment should stop
 *  after index b−1(that is 1 after the range).
 *  Prefix Sum: Calculating the prefix sum accumulates these increments, effectively
 *  applying the range increments as if we had incremented each element directly within
 *  the range for each query.
 */
public class RangeUpdateAndPrefixSum {
    public static long arrayManipulation (List<List<Integer>> queries) {
        Map<Long,Long> map=new TreeMap<>();
        long max=0;
        for (List<Integer> input:queries){
            map.put(input.get(0) - 1L,
                    map.getOrDefault(input.get(0) - 1L, 0L) + input.get(2));
            map.put(Long.valueOf(input.get(1)),
                    map.getOrDefault(Long.valueOf(input.get(1)), 0L) - input.get(2));
        }

        long sum=0;
        for (Map.Entry<Long, Long> i:map.entrySet()){
            sum+=i.getValue();
            if (sum>max)
                max=sum;
        }

        return max;


    }

    public static void main(String[] args) {
        ArrayList<List<Integer>> inputs=new ArrayList<>();
        inputs.add(Arrays.asList(1,2,100));
        inputs.add(Arrays.asList(2,5,100));
        inputs.add(Arrays.asList(3,4,100));

        System.out.println(arrayManipulation(inputs));
    }
}
