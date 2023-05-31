package markup;

import java.util.List;

public class UnorderedList implements ListSubitem {
    private final List<ListItem> contents;

    public UnorderedList(List<ListItem> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "ul", contents);
    }
}
