package markup;

import java.util.List;

public class Paragraph implements ListSubitem, Markdownable {
    private final List<ParagraphContent> contents;

    public Paragraph(List<ParagraphContent> contents) {
       this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        for (ParagraphContent content : contents) {
            content.toHtml(sb);
        }
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        for (ParagraphContent content : contents) {
            content.toMarkdown(sb);
        }
    }
}
