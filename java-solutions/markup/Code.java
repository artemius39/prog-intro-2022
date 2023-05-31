package markup;

import java.util.List;

public class Code implements ParagraphContent {
    List<ParagraphContent> contents;

    public Code(List<ParagraphContent> contents) {
        this.contents = contents;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        Markdown.toMarkdown(sb, "`", contents);
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "code", contents);
    }
}
