package md2html;

import markup.*;
import java.util.*;

public class TagParser {

    static class TagBlock {
        TagBlock outer;
        List<ParagraphContent> contents = new LinkedList<>();

        public TagBlock() {
            this.outer = this;
        }

        public TagBlock(TagBlock outer) {
            this.outer = outer;
        }

        public void add(ParagraphContent x) {
            contents.add(x);
        }
    }

    private TagBlock current = new TagBlock();
    private Deque<String> markdownTags = new ArrayDeque<>();
    private Set<String> opened = new HashSet<>();
    private final String PARAGRAPH_TAG = "";
    private Image currentImage;

    public TagParser() {
        markdownTags.push(PARAGRAPH_TAG);
        opened.add(PARAGRAPH_TAG);
    }

    private ParagraphContent getClass(String tag) {
        if (tag.equals("**") || tag.equals("__")) {
            return new Strong(current.contents);
        }
        if (tag.equals("*") || tag.equals("_")) {
            return new Emphasis(current.contents);
        }
        if (tag.equals("`")) {
            return new Code(current.contents);
        }
        if (tag.equals("--")) {
            return new Strikeout(current.contents);
        }
        return null;
    }

    public void parseTag(StringBuilder text, char... chars) {
        parseTag(text, String.valueOf(chars));
    }

    public void startImage(StringBuilder text) {
        currentImage = new Image();
        current.add(new Text(text.toString()));
        text.setLength(0);
    }

    public void parseImageName(StringBuilder text) {
        currentImage.setName(text.toString());
        text.setLength(0);
    }

    public void parseImageLink(StringBuilder text) {
        currentImage.setLink(text.toString());
        text.setLength(0);
        current.add(currentImage);
        currentImage = null;
    }

    public void parseTag(StringBuilder text, String tag) {
        current.add(new Text(text.toString()));
        text.setLength(0);
        if (opened.contains(tag)) {
            while (!markdownTags.peek().equals(tag)) {
                opened.remove(markdownTags.peek());
                current.outer.add(new Text(markdownTags.peek()));
                markdownTags.pop();
                current.outer.contents.addAll(current.contents);
                current = current.outer;
            }
            opened.remove(tag);
            if (!tag.equals(PARAGRAPH_TAG)) {
                current.outer.add(getClass(tag));
                current = current.outer;
                markdownTags.pop();
            }
        } else {
            current = new TagBlock(current);
            markdownTags.push(tag);
            opened.add(tag);
        }
    }

    public Paragraph getParagraph(StringBuilder text) {
        parseTag(text, PARAGRAPH_TAG);
        return new Paragraph(current.contents);
    }
}
