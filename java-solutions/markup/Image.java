package markup;

public class Image implements ParagraphContent {
    String name, link;

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image() {
    }

    @Override
    public void toHtml(StringBuilder sb) {
        sb.append("<img alt='").append(name).append("' src='").append(link).append("'>");
    }

    @Override
    public void toMarkdown(StringBuilder sb) {

    }
}
