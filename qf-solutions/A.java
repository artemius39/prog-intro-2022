import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt(), b = in.nextInt(), n = in.nextInt();
        System.out.println(2 * ((n - b) / (b - a) + (((n - b) % (b - a) == 0) ? 0 : 1)) + 1);
    }
}
