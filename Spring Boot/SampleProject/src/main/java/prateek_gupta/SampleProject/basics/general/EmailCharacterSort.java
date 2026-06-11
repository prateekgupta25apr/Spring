package prateek_gupta.SampleProject.basics.general;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailCharacterSort {
    /**
     * EmailSort
     * <br>
     * Question:
     * First get all the alphabets of the email then sort the alphabets then without changing the
     * position of the special characters and numbers just replace alphabets
     * <br>
     * Examples
     * <br>
     * Input: pencil@gmail.com
     * Output : accegi@illmm.nop
     * <br>
     * Input : hi.hello@gmail.com
     * Output : ac.eghhi@illlm.moo
     * <br>
     * Topics : Regex, StringBuilder
     */
    static String emailCharacterSort(String email) {
        Pattern charactersRegex = Pattern.compile("[a-zA-Z]");
        Matcher matcher;
        List<String> characters = new ArrayList<>();
        List<Integer> charactersPos = new ArrayList<>();
        List<String> emailChars = Arrays.asList(email.split(""));
        for (int i = 0; i < emailChars.size(); i++) {
            matcher = charactersRegex.matcher(emailChars.get(i));
            if (matcher.find()) {
                String character = matcher.group();
                if (character != null) {
                    characters.add(character);
                    charactersPos.add(i);
                }
            }
        }
        Collections.sort(characters);
        StringBuilder stringBuilder = new StringBuilder(email);
        for (int i = 0; i < charactersPos.size(); i++) {
            stringBuilder.setCharAt(charactersPos.get(i), characters.get(i).charAt(0));
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println("pencil@gmail.com : " +
                EmailCharacterSort.emailCharacterSort("pencil@gmail.com"));

        System.out.println("hi.hello@gmail.com : " +
                EmailCharacterSort.emailCharacterSort("hi.hello@gmail.com"));
    }
}
