package markup;

import java.util.List;

// strong __
public class Strong implements ParagraphContent {
    private final List<ParagraphContent> contents;

    public Strong(List<ParagraphContent> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "strong", contents);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        Markdown.toMarkdown(sb, "__", contents);
    }
}
