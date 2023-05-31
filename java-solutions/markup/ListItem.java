package markup;

import java.util.List;

public class ListItem implements Htmlable {
    private final List<ListSubitem> contents;

    public ListItem(List<ListSubitem> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "li", contents);
    }
}
