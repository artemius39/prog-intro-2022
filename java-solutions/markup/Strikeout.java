package markup;

import java.util.List;

// s ~
public class Strikeout implements ParagraphContent {
    private final List<ParagraphContent> contents;

    public Strikeout(List<ParagraphContent> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "s", contents);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        Markdown.toMarkdown(sb, "~", contents);
    }
}