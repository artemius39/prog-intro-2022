package markup;

import java.util.List;

public abstract class Markdown implements Markdownable {
    public static void toMarkdown(StringBuilder sb, String markdownTag, List<? extends Markdownable> contents) {
        sb.append(markdownTag);
        for (Markdownable content : contents) {
            content.toMarkdown(sb);
        }
        sb.append(markdownTag);
    }
}
