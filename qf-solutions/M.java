import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class M {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int test = 0; test < t; test++) {
            int n = in.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            Map<Integer, Integer> C = new HashMap<>();
            long ans = 0;
            for (int j = n - 1; j > 0; j--) {
                for (int i = 0; i < j; i++) {
                    ans += C.getOrDefault(2 * a[j] - a[i], 0);
                }
                C.put(a[j], C.getOrDefault(a[j], 0) + 1);
            }
            System.out.println(ans);
        }
    }
}
