import java.util.Scanner;

public class I {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
//        int[] x = new int[n], y = new int[n], h = new int[n];
        int xl = Integer.MAX_VALUE, xr = Integer.MIN_VALUE, yl = Integer.MAX_VALUE, yr = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int hi = in.nextInt();
            xl = Integer.min(x - hi, xl);
            xr = Integer.max(x + hi, xr);
            yl = Integer.min(y - hi, yl);
            yr = Integer.max(y + hi, yr);
        }
        int a = Integer.max(xr - xl, yr - yl);
        System.out.println((xl + xr) / 2 + " " + (yr + yl) / 2 + " " + (a / 2 + a % 2));
    }
}
