package markup;

public class Text implements ParagraphContent {
    private final String contents;

    public Text(String contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        sb.append(contents);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(contents);
    }
}
