import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Scanner {
    private Reader reader;
    private final int BUFFER_SIZE = 1024;
    private char[] buffer = new char[BUFFER_SIZE]; // buffer stores characters read from file
    private int current = 0; // pointer to the first unread character
    private int read; // How many characters were read last time
    // Token types
    private final int WORD = 1; // word according to WordStat criteria
    private final int NUMBER = 2;
    private final int LINE_BREAK = 3;
    private final int OTHER = 0;
    // These constants are passed as arguments that tell
    // where hasNext/hasNextInt/hasNextWord should search for character/number/word
    private final static int IN_FILE = 0;
    private final static int IN_LINE = 1;
    private int begin; // the beginning of the part of current token that is stored in current buffer
    private StringBuilder tokenPart; // StringBuilder stores the parts that were in previous buffers
    // tells whether a minus at the beginning of a number has been found
    private boolean minusFound = false;
    
    public Scanner(File source) throws FileNotFoundException, IOException {
        this(new FileInputStream(source));
    }
    
    public Scanner(InputStream source) throws IOException {
        reader = new InputStreamReader(source, StandardCharsets.UTF_8);
        read = reader.read(buffer);
    }
    
    private int type(char c) {
        if (c == '\n' || c == '\r' || c == '\u2028' || c == '\u2029' || c == '\u0085') {
            return LINE_BREAK;
        }
        if (Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION) {
            return WORD;
        }
        if (Character.isDigit(c)) {
            return NUMBER;
        }
        return OTHER;
    }
    // token type is defined as type of any of its characters
    private int type(String token) {
        return type(token.charAt(0));
    }
    
    private String next() throws IOException {
        if (read < 0) {
            throw new NoSuchElementException("Attempt to read from an empty stream");
        }
        tokenPart = new StringBuilder();
        begin = current;
        char first = buffer[current]; // first character of the token
        minusFound = minusFound || first == '-';
        int tokenType = type(first);
        add();
        // handling \r\n
        if (tokenType == LINE_BREAK) {
            if (first == '\r' && buffer[current] == '\n') {
                add();
            }
            tokenPart.append(buffer, begin, current - begin);
            return new String(tokenPart);
        }
        while (hasNext() && type(buffer[current]) == tokenType) {
            add();
        }
        if (tokenType != OTHER) {
            tokenPart.append(buffer, begin, current - begin);
            return new String(tokenPart);
        } else {
            // it doesn't matter what kind of garbage we return
            return " ";
        }
    }
    
    private void add() throws IOException {
        current++;
        if (current == read) {
            // there is no need to store garbage in StringBuilder
            if (type(buffer[current - 1]) != OTHER) {
                tokenPart.append(buffer, begin, current - begin);
            }
            begin = 0;
            current = 0;
            read = reader.read(buffer);
        }
    }
    
    public String nextWord() throws NoSuchElementException, IOException {
        if (!hasNextWord()) {
            throw new NoSuchElementException("Attempt to read a word when there is none");
        }
        String token = next();
        while (type(token) != WORD) {
            token = next();
        }
        return token;
    }
    
    public int nextInt() throws NoSuchElementException, IOException  {
        if (!hasNextInt()) {
            throw new NoSuchElementException("Attempt to read an int when there is none");
        }
        String token;
        token = next();
        while (type(token) != NUMBER) {
            token = next();
        }
        if (hasNext() && Character.toLowerCase(buffer[current]) == 'o') {
            return Integer.parseUnsignedInt(token, 8);
        } else {
            if (minusFound) {
                minusFound = false;
                return -Integer.parseInt(token);
            }
            return Integer.parseInt(token);
        }
    }
    
    public boolean hasNext() {
        return hasNext(IN_FILE);
    }
    
    public boolean hasNextInLine() {
        return hasNext(IN_LINE);
    }
    
    private boolean hasNext(int inLineOrInFile) {
        if (inLineOrInFile == IN_FILE) {
            return read >= 0;
        } else {
            return read >= 0 && type(buffer[current]) != LINE_BREAK;
        }
    }
    
    public boolean hasNextWord() throws IOException {
        return hasNextWord(IN_FILE);
    }
    
    public boolean hasNextWordInLine() throws IOException {
        return hasNextWord(IN_LINE);
    }
    
    private boolean hasNextWord(int inLineOrInFile) throws IOException, NoSuchElementException {
        while (hasNext(inLineOrInFile) && type(buffer[current]) != WORD) {
            next();
        }
        return hasNext(inLineOrInFile);
    }
    
    public boolean hasNextInt() throws IOException {
        return hasNextInt(IN_FILE);
    }
    
    public boolean hasNextIntInLine() throws IOException {
        return hasNextInt(IN_LINE);
    }
    
    private boolean hasNextInt(int inLineOrInFile) throws IOException, NoSuchElementException {
        while (hasNext(inLineOrInFile) && type(buffer[current]) != NUMBER) {
            next();
        }
        return hasNext(inLineOrInFile);
    }
    
    // Advances scanner to next line without returning what was skipped
    public void nextLine() throws NoSuchElementException, IOException {
        while (hasNextInLine()) {
            next();
        }
        if (!hasNext()) {
            throw new NoSuchElementException("Attempt to move to next line when there are no more left");
        }
        next();
    }
    
    public void close() throws IOException {
        reader.close();
    }
}
