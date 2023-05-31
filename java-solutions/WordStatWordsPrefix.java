import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatWordsPrefix {
    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileName = args[1];
        Map<String, Integer> stat = new TreeMap<>();
        try {
            Scanner in = new Scanner(new File(inputFileName));
            try {
                while (in.hasNextWord()) {
                    String newWord = in.nextWord().toLowerCase();
                    newWord = newWord.substring(0, Integer.min(newWord.length(), 3));
                    stat.put(newWord, stat.getOrDefault(newWord, 0) + 1);
                }
            } catch (NoSuchElementException e) {
                System.out.println("No such element exception while reading words: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("I/O exception while reading words: " + e.getMessage());
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O exception: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputFileName), StandardCharsets.UTF_8
                    )
            );
            try {
                for (String word: stat.keySet()) {
                    Integer currentStat = stat.get(word);
                    writer.write(word + " " + currentStat);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("I/O Exception while writing answer: " + e.getMessage());
                e.printStackTrace();
            } finally {
                writer.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
