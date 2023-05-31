import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileName = args[1];
        Map<String, Integer> stat = new LinkedHashMap<>();
        try {
            Scanner in = new Scanner(new File(inputFileName));
            try {
                while (in.hasNextWord()) {
                    String newWord = in.nextWord().toLowerCase();
                    stat.put(newWord, stat.getOrDefault(newWord, 0) + 1);
                }
            } catch (NoSuchElementException e) {
                System.err.println("No such element exception while reading words: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("I/O exception while reading words: " + e.getMessage());
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O exception: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputFileName), StandardCharsets.UTF_8
                    )
            );
            try {
                for (String word : stat.keySet()) {
                    writer.write(word + " " + stat.get(word));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("I/O exception while writing answer: " + e.getMessage());
            } finally {
                writer.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

