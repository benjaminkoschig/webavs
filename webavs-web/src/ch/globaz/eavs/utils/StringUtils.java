package ch.globaz.eavs.utils;

public final class StringUtils {
    public final static String firstLetterToUpperCase(String s) {
        if (isBlank(s)) {
            return s;
        }
        if (s.length() <= 1) {
            return s.toUpperCase();
        }
        String part1 = s.substring(0, 1);
        String part2 = s.substring(1);
        return part1.toUpperCase() + part2;
    }

    public final static boolean isBlank(String s) {
        return !((s != null) && (s.trim().length() > 0));
    }

    public static boolean isInteger(String integer) {
        if (StringUtils.isBlank(integer)) {
            return false;
        }
        if (!integer.matches("\\s*-?\\s*\\d*\\s*")) {
            return false;
        }
        try {
            Integer.parseInt(integer);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isNumeric(String numeric) {
        if (StringUtils.isBlank(numeric)) {
            return false;
        }
        if (!numeric.matches("\\s*-?\\s*\\d*\\.?\\d*\\s*")) {
            return false;
        }
        try {
            Double.parseDouble(numeric);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public final static String removeChar(String s, char c) {
        if (s == null) {
            return s;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch != c) {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    private StringUtils() {
        // utility class, hidden
    }
}
