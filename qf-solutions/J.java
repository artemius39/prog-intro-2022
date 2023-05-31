import java.util.Scanner;

public class J {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] p = new int[n][n];
        for (int i = 0; i < n; i++) {
            String line = in.next();
            for (int j = 0; j < n; j++) {
                p[i][j] = Character.digit(line.charAt(j), 10);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i + 1; j++) {
                System.out.print(0);
            }
            for (int j = i + 1; j < n; j++) {
                if (p[i][j] == 0) {
                    System.out.print(0);
                } else {
                    System.out.print(1);
                    for (int k = j + 1; k < n; k++) {
                        p[i][k] = (p[i][k] - p[j][k] + 10) % 10;
                    }
                }
            }
            System.out.println();
        }
    }
}