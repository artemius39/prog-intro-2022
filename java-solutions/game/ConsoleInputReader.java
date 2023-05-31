package game;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleInputReader {
    private Scanner in;

    public ConsoleInputReader() {
        this.in = new Scanner(System.in);
    }

    public int[] readInts(int n, String prompt) {
        while(true) {
            System.out.println(prompt);
            Scanner line;
            while (true) {
                if (in.hasNextLine()) {
                    line = new Scanner(in.nextLine());
                    break;
                }
                in = new Scanner(System.in);
                System.out.println("Hey, don't do that!");
            }
            int[] ints = new int[n];
            int i;
            for (i = 0; line.hasNextInt() && i < n; i++) {
                ints[i] = line.nextInt();
            }
            if (i == n && !line.hasNext()) {
                return ints;
            }
            System.out.println("Invalid input format, try again.");
        }
    }

    public int readInt(String prompt) {
        return readInts(1, prompt)[0];
    }
}
