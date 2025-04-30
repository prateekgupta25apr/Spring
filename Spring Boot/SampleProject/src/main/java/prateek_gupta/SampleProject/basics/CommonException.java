package src;

import java.util.Scanner;

public class CommonException {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        byte userChoice;
        char userWish;
        do {
            System.out.println(
                    "**Welcome to Exception Testing**\n" +
                            "Enter\n" +
                            "1. CustomException\n" +
                            "2. CustomRuntimeException\n" +
                            "3.StackoverflowException\n" +
                            "4.ArrayIndexOutOfBoundsException\n" +
                            "5.StringIndexOutOfBoundsException\n" +
                            "6.InputMismatchException\n" +
                            "7.NullPointerException\n" +
                            "8.ArithmeticException\n" +
                            "9.NumberFormatException");
            userChoice = scanner.nextByte();

            // To clean up the buffer
            scanner.nextLine();

            switch (userChoice) {
                case 1 : throw new CustomException();
                case 2: throw new CustomRunException();
                case 3 : produceStackoverflowException(1);break;
                case 4 : produceArrayIndexOutOfBoundsException();break;
                case 5 : System.out.println("prateek".charAt(25));break;
                case 6 : produceInputMismatchException();break;
                case 7 : produceNullPointerException();break;
                case 8 : int i = 25 / 0;break;
                case 9 : int j = Integer.parseInt("prateek");break;

            }
            System.out.println("Enter Y/y to continue or any other character to exit");
            userWish = scanner.nextLine().charAt(0);
        } while (userWish == 'Y' || userWish == 'y');
    }

    static void produceStackoverflowException(int i) {
        produceStackoverflowException(++i);
    }

    static void produceArrayIndexOutOfBoundsException() {
        int[] a = {1};
        for (int i = 0; i <= a.length; i++)
            System.out.println(a[i]);
    }

    static void produceInputMismatchException() {
        System.out.println("Enter your name");
        int i = scanner.nextInt();

    }

    static void produceNullPointerException() {
        String s = null;
        System.out.println(s.charAt(0));

    }

    public static class CustomException extends Exception{
        @Override
        public String getMessage() {
            return "Custom Exception thrown";
        }
    }
    static class CustomRunException extends RuntimeException{
        @Override
        public String getMessage() {
            return "Custom RunException thrown";
        }
    }
}
