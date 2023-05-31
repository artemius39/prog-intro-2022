package markup;

import java.util.List;

public class OrderedList implements ListSubitem {
    private final List<ListItem> contents;

    public OrderedList(List<ListItem> contents) {
        this.contents = contents;
    }

    @Override
    public void toHtml(StringBuilder sb) {
        Html.toHtml(sb, "ol", contents);
    }
}
