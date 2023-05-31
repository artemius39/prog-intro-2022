public class Sum {
    public static void main(String[] args) {
        int ans = 0;
        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                while (i < arg.length() && Character.isWhitespace(arg.charAt(i))) {
                    i++;
                }
                if (i >= arg.length()) {
                    break;
                }

                int l = i;
                boolean isNegative = arg.charAt(i) == '-';
                if (arg.charAt(i) == '+' || arg.charAt(i) == '-') {
                    i++;
                }

                while (i < arg.length() && Character.isDigit(arg.charAt(i))) {
                    i++;
                }
                ans += isNegative ?
                        Integer.parseInt(arg.substring(l, i)) :
                        Integer.parseUnsignedInt(arg.substring(l, i));
            }
        }
        System.out.println(ans);
    }
}
