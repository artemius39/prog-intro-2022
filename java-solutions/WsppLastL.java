import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppLastL {
    public static void main(String[] args) {
        String inputFileName = args[0];
        String outputFileName = args[1];
        HashMap<String, IntList> stat = new LinkedHashMap<>(); // :NOTE: Use not interface in type
        HashMap<String, Integer> numberOfOccurences = new LinkedHashMap<>();
        try {
            Scanner in = new Scanner(new File(inputFileName));
            try {
                int i = 0;
                int lineBegin = 0;
                while (in.hasNextWord()) {
                    HashMap<String, Integer> lineStat = new LinkedHashMap<>();
                    HashMap<String, Integer> occurrencesInThisLine = new LinkedHashMap<>();
                    while (in.hasNextWordInLine()) {
                        try {
                            String newWord = in.nextWord().toLowerCase();
                            lineStat.put(newWord, i + 1 - lineBegin);
                            occurrencesInThisLine.put(newWord, occurrencesInThisLine.getOrDefault(newWord, 0) + 1);
                            i++;
                        } catch (NoSuchElementException e) {
                            System.err.println("No such element exception while reading words: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    for (String word : lineStat.keySet()) {
                        IntList occurrences = stat.get(word);
                        if (occurrences == null) {
                            occurrences = new IntList();
                        }
                        occurrences.add(lineStat.get(word));
                        stat.put(word, occurrences);
                        numberOfOccurences.put(
                            word,
                            numberOfOccurences.getOrDefault(word, 0) + occurrencesInThisLine.get(word)
                        );
                        lineBegin = i;
                    }
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println("I/O exception while closing scanner: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Excpetion: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFileName),
                StandardCharsets.UTF_8
            ));
            try {
                for (String word : stat.keySet()) {
                    IntList occurrences = stat.get(word);
                    writer.write(word + " " + numberOfOccurences.get(word));
                    for (int i = 0; i < occurrences.size(); i++) {
                        writer.write(" " + occurrences.get(i));
                    }
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("I/O Exception: " + e.getMessage());
            } finally {
                writer.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output error: " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported input encoding: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O Exception: " + e.getMessage());
        }
    }
}

