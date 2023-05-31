package markup;

import java.util.List;

// em *
public class Emphasis implements ParagraphContent {
    private final List<ParagraphContent> contents;

    public Emphasis(List<ParagraphContent> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "em", contents);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        Markdown.toMarkdown(sb, "*", contents);
    }
}
