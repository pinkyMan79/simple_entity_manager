package one.terenin;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int[] aa = readInput();
        int count = countUnique(aa);
        System.out.println(count);
    }


    private static int[] readInput() {
        int lines = sc.nextInt();
        int[] a = new int[lines];
        for (int i = 0; i < lines; i++) {
            a[i] = sc.nextInt();
        }
        return a;
    }

    public static int countUnique(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int j : arr) {
            if (map.containsKey(j)) {
                map.put(j, map.get(j) + 1);
            } else {
                map.put(j, 1);
            }
        }
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                count++;
            }
        }
        return count;
    }
}