import java.io.*;
import java.util.*;

public class ReverseOctDec {
    public static void main(String[] args) {
        int[][] numbers = new int[1][];
        int[] tmp = new int[1];
        int numberOfLines = 0;
        try {
            Scanner in = new Scanner(System.in);
            try {
                for (numberOfLines = 0; in.hasNext(); numberOfLines++) {
                    int numberOfNumbers;
                    for (numberOfNumbers = 0; in.hasNextIntInLine(); numberOfNumbers++) {
                        if (numberOfNumbers == tmp.length) {
                            tmp = Arrays.copyOf(tmp, tmp.length * 2);
                        }
                        tmp[numberOfNumbers] = in.nextInt();
                    }
                    numbers[numberOfLines] = new int[numberOfNumbers];
                    System.arraycopy(tmp, 0, numbers[numberOfLines], 0, numberOfNumbers);
                    if (numberOfLines == numbers.length - 1) {
                        numbers = Arrays.copyOf(numbers, numbers.length * 2);
                    }
                    if (in.hasNext()) {
                        in.nextLine();
                    }
                }
            } catch (IOException e) {
                System.err.println("I/O exception while reading numbers: " + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.err.println("No such element exception while reading numbers: " + e.getMessage());
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.err.println("I/O exception: " + e.getMessage());
        }
        
        for (int i = numberOfLines - 1; i >= 0; i--) {
            for (int j = numbers[i].length - 1; j >= 0; j--) {
                System.out.print(numbers[i][j] + " ");
            }
            System.out.println();
        }
    }
}

