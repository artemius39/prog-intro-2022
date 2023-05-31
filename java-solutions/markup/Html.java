package markup;

import java.util.List;

public abstract class Html implements Htmlable {
    public static void toHtml(StringBuilder sb, String htmlTag, List<? extends Htmlable> contents) {
        sb.append("<").append(htmlTag).append(">");
        for (Htmlable content : contents) {
            content.toHtml(sb);
        }
        sb.append("</").append(htmlTag).append(">");
    }
}
