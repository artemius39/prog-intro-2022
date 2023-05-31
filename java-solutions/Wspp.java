import java.io.*;
import java.util.*;

public class Wspp {
	public static void main(String[] args) {
		String inputFileName = args[0];
		String outputFileName = args[1];
		LinkedHashMap<String, IntList> stat = new LinkedHashMap<String, IntList>();
		try {
			Scanner in = new Scanner(new File(inputFileName));
			try {
				int i = 0;
				while (in.hasNextWord()) {
					try {
						String newWord = in.nextWord().toLowerCase();
						IntList occurences = stat.get(newWord);
						if (occurences == null) {
							occurences = new IntList();
						}
						occurences.add(i + 1);
						stat.put(newWord, occurences);
						i++;
					} catch (NoSuchElementException e) {
						System.err.println("No such element exception while reading words: " + e.getMessage());
						e.printStackTrace();
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
			System.err.println("I/O Exception: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "utf8"));
			try {
				for (String word : stat.keySet()) {
					IntList occurences = stat.get(word);
					writer.write(word + " " + occurences.size());
					for (int i = 0; i < occurences.size(); i++) {
						writer.write(" " + occurences.get(i));
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

