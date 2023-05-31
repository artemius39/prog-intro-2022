import java.io.IOException;
import java.util.*;

public class ReverseSum {
    public static void main(String[] args) {
        int[][] numbers = new int[1][];
        int[] sumByLine = new int[1], sumByColumn = new int[1];
        int[] tmp = new int[1];
        int numberOfLines = 0;
        try {
            Scanner in = new Scanner(System.in);
            try {
                for (numberOfLines = 0; in.hasNext(); numberOfLines++) {
                    int numberOfNumbers;
                    if (numberOfLines >= sumByLine.length) {
                        sumByLine = Arrays.copyOf(sumByLine, 2 * sumByLine.length);
                    }
                    for (numberOfNumbers = 0; in.hasNextIntInLine(); numberOfNumbers++) {
                        if (numberOfNumbers == tmp.length) {
                            tmp = Arrays.copyOf(tmp, 2 * tmp.length);
                        }
                        int number = in.nextInt();
                        tmp[numberOfNumbers] = number;
                        sumByLine[numberOfLines] += number;
                        if (numberOfNumbers >= sumByColumn.length) {
                            sumByColumn = Arrays.copyOf(sumByColumn, 2 * numberOfNumbers);
                        }
                        sumByColumn[numberOfNumbers] += number;
                    }
                    if (numberOfNumbers == tmp.length - 1) {
                        tmp = Arrays.copyOf(tmp, tmp.length * 2);
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
        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                System.out.print(sumByLine[i] + sumByColumn[j] - numbers[i][j] + " ");
            }
            System.out.println();
        }
    }
}

