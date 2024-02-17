package gestorInventario.utils;

import java.util.Scanner;

public final class Utils {
    static Scanner sc;

    private Utils() { }

    static {
        sc = new Scanner(System.in);
    }

    public static int getInt(String prompt) {
        System.out.print(prompt);
        while(!sc.hasNextInt())
            sc.next();

        int val = sc.nextInt();
        sc.nextLine();

        return val;
    }

    public static String getString(String prompt) {
        String val;

        do {
            System.out.print(prompt);
            val = sc.nextLine().trim();
        } while(val.isEmpty());

        return val;
    }

    public static String getAnyString(String prompt) {
        System.out.print(prompt);

        return sc.nextLine();
    }

}
