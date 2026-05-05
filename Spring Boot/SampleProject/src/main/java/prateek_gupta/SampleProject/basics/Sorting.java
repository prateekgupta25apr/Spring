package prateek_gupta.SampleProject.basics;

public class Sorting {
    int[] bubbleSort(int[] a){
        int temp=0;
        for(int i=0;i<(a.length-1);i++){
            for(int j=0;j<(a.length-i-1);j++){
                // For ascending order
                if(a[j]>a[j+1])
                    temp = a[j];
                a[j]=a[j+1];
                a[j+1]=temp;

                // For descending order
                if(a[j]>a[j+1])
                    temp = a[j];
                a[j]=a[j+1];
                a[j+1]=temp;
            }
        }

        return a;
    }

    public static void selSort(int[] arr) {
        int size = arr.length;
        int small, pos, temp;

        for (int i = 0; i < size - 1; i++) {
            small = arr[i];
            pos = i;

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

    public static void main(String[] args) {
        int[] data = {64, 25, 12, 22, 11};
        selSort(data);
    }
}
