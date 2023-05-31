package md2html;

import markup.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {

    static String currentLine;
    // pointer to first unread character
    static int current = 0;
    private static BufferedReader in;
    private static StringBuilder text;
    static List<ParagraphContent> paragraphContents;
    // tells where the unclosed tag is located in paragraphContents
    static Map<String, Integer> openingTagPosition;
    static final List<String> VALID_TAGS = List.of("**", "__", "--", "*", "_", "`"); // :NOTE: modifier
    static final List<Character> escapableCharacters = List.of('*', '_', '`');
    static final List<Character> htmlEscapableCharacters = List.of('<', '>', '&');
    static final Map<Character, String> htmlEscapeReplacement = Map.of(
            '<', "&lt;",
            '>', "&gt;",
            '&', "&amp;"
    );

    // advances pointer n characters forward
    static void advance(int n) throws IOException {
        current += n;
        if (current >= currentLine.length()) {
            currentLine = in.readLine();
            if (currentLine != null && !currentLine.isEmpty()) {
                text.append('\n');
            }
            current = 0;
        }
    }

    // wraps contents in the corresponding class
    private static ParagraphContent wrap(String tag, List<ParagraphContent> contents) {
        if (tag.equals("**") || tag.equals("__")) {
            return new Strong(contents);
        }
        if (tag.equals("*") || tag.equals("_")) {
            return new Emphasis(contents);
        }
        if (tag.equals("`")) {
            return new Code(contents);
        }
        if (tag.equals("--")) {
            return new Strikeout(contents);
        }
        return null;
    }

    static void parseTag(String tag) {
        paragraphContents.add(new Text(text.toString()));
        text.setLength(0);

        Integer pos = openingTagPosition.get(tag);
        // non-null pos means there is an unclosed tag, so this tag will be closing
        // parsing closing tag
        if (pos != null) {
            // these tags will never be closed
            for (String unclosedTag : openingTagPosition.keySet()) {
                if (openingTagPosition.get(unclosedTag) > pos) {
                    openingTagPosition.remove(unclosedTag);
                }
            }
            List<ParagraphContent> tmp = new ArrayList<>(paragraphContents.subList(pos + 1, paragraphContents.size()));
            paragraphContents.subList(pos, paragraphContents.size()).clear();
            paragraphContents.add(wrap(tag, tmp));
            openingTagPosition.remove(tag);
        // parsing opening tag
        } else {
            paragraphContents.add(new Text(tag));
            openingTagPosition.put(tag, paragraphContents.size() - 1);
        }
    }

    public static void main(final String[] args) {
        final String mdInputFileName = args[0];
        final String htmlOutputFileName = args[1];
        try {
            in = new BufferedReader(new FileReader(mdInputFileName, StandardCharsets.UTF_8)); // :NOTE: убрать статики
            try (BufferedWriter out = new BufferedWriter(new FileWriter(htmlOutputFileName, StandardCharsets.UTF_8))) {
                // parsing paragraphs & headers
                for (currentLine = in.readLine(); ; currentLine = in.readLine()) {
                    // skipping empty lines
                    while (currentLine != null && currentLine.isEmpty()) {
                        currentLine = in.readLine();
                    }
                    // null line means end of file
                    if (currentLine == null) {
                        break;
                    }

                    current = 0;
                    text = new StringBuilder();
                    paragraphContents = new ArrayList<>();
                    openingTagPosition = new HashMap<>();

                    // parsing header
                    int headerLevel = 0;
                    while (currentLine != null && currentLine.charAt(current) == '#') {
                        text.append('#');
                        advance(1);
                        headerLevel++;
                    }
                    if (currentLine != null && currentLine.charAt(current) == ' ' && 0 < headerLevel && headerLevel <= 6) {
                        advance(1);
                        text.setLength(0);
                    } else {
                        headerLevel = 0;
                    }

                    // parsing characters
                    for (; currentLine != null && !currentLine.isEmpty(); advance(1)) {
                        // parsing image
                        if (current + 1 < currentLine.length() && currentLine.startsWith("![", current)) {
                            advance(2);
                            paragraphContents.add(new Text(text.toString()));
                            text.setLength(0);
                            Image image = new Image();

                            // parsing image name
                            while (!currentLine.startsWith("](", current)) {
                                text.append(currentLine.charAt(current));
                                advance(1);
                            }
                            image.setName(text.toString());
                            text.setLength(0);
                            advance(2);

                            // parsing image link
                            while (currentLine.charAt(current) != ')') {
                                text.append(currentLine.charAt(current));
                                advance(1);
                            }
                            image.setLink(text.toString());

                            text.setLength(0);
                            paragraphContents.add(image);

                        // parsing 2 character long tags
                        } else if (current + 1 < currentLine.length() && VALID_TAGS
                            .contains(currentLine.substring(current, current + 2))) {
                            parseTag(currentLine.substring(current, current + 2));
                            advance(1);

                        // parsing 1 character long tags
                        } else if (VALID_TAGS.contains(currentLine.substring(current, current + 1))) {
                            parseTag(currentLine.substring(current, current + 1));

                        // parsing html escapable characters
                        } else if (htmlEscapableCharacters.contains(currentLine.charAt(current))) {
                            text.append(htmlEscapeReplacement.get(currentLine.charAt(current)));

                        // parsing markdown escapable characters
                        } else if ( current + 1 < currentLine.length() &&
                                    currentLine.charAt(current) == '\\' &&
                                    escapableCharacters.contains(currentLine.charAt(current + 1))) {
                            text.append(currentLine.charAt(current + 1));
                            advance(1);

                        // parsing normal characters
                        } else {
                            text.append(currentLine.charAt(current));
                        }
                    }
                    paragraphContents.add(new Text(text.toString()));
                    text.setLength(0);
                    final StringBuilder html = new StringBuilder();
                    new Paragraph(paragraphContents).toHtml(html);
                    if (headerLevel == 0) {
                        out.write("<p>" + html + "</p>");
                    } else {
                        out.write("<h" + headerLevel + ">" + html + "</h" + headerLevel + ">");
                    }
                    out.newLine();
                }
            } catch (IOException e) {
                System.err.println("I/O Exception while reading or writing characters: " + e.getMessage());
            } finally {
                in.close();
            }
        } catch (final FileNotFoundException e) {
            System.err.println("File not found exception: " + e.getMessage());
        } catch (final IOException e) {
            System.err.println("I/O Exception: " + e.getMessage());
        }
    }
}