package one.terenin;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Main1 {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        String number = sc.nextLine();
        int tries = sc.nextInt();
        int allPermutations = IntStream.rangeClosed(2, number.length()).reduce((x, y) -> x * y)
                .orElseThrow(); // P(n)
        int goodPermutations = 0;
        Set<String> numSet = new HashSet<>();
        for (int i = 0; i < number.length(); i++) {
            for (int j = 0; j < number.length(); j++) {
                if (check6(number) || check5(number) || check10(number)){
                    numSet.add(number);
                }
                number = swapChars(number, i, j);
            }
        }

        goodPermutations = numSet.size();
        double res = ((double) goodPermutations / (double) allPermutations) * tries;

        System.out.println(res);

    }

    public static boolean check5(String line){
        return Integer.parseInt(String.valueOf(line.charAt(line.length() - 1))) == 5;
    }
    public static boolean check2(String line){
        return Integer.parseInt(String.valueOf(line.charAt(line.length() - 1))) % 2 == 0;
    }
    public static boolean check6(String line){
        int numsum = 0;
        for (int i = 0; i < line.length(); i++) {
            numsum += Integer.parseInt(String.valueOf(line.charAt(i)));
        }
        return check2(line) && numsum % 3 == 0;
    }
    public static boolean check10(String line){
        return Integer.parseInt(String.valueOf(line.charAt(line.length() - 2))
                + line.charAt(line.length() - 1)) == 5;
    }
    private static String swapChars(String str, int lIdx, int rIdx) {
        StringBuilder sb = new StringBuilder(str);
        char l = sb.charAt(lIdx), r = sb.charAt(rIdx);
        sb.setCharAt(lIdx, r);
        sb.setCharAt(rIdx, l);
        return sb.toString();
    }
}
