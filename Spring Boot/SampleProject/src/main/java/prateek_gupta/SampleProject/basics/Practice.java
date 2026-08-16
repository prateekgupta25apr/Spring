package prateek_gupta.SampleProject.basics;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Practice {
    public static void main(String[] args) {
        TreeSet<Integer> list = new TreeSet<>();
        list.add(100);
        list.add(200);
        list.add(500);
        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println(list);

//        int maxLength = 0;
//        for (int i = 0; i <= list.size(); i++) {
//            int j=i+1;
//            while (list.get(j)-list.get(i)==1){
//                maxLength++;
//                i=j;
//                j=j+1;
//                if (i>list.size()||j>list.size()){
//                    break;
//                }
//            }
//        }
//
//        System.out.println(maxLength);

        Pattern pattern = Pattern.compile("P[a-z]+");

        System.out.println(pattern);

    }
}



