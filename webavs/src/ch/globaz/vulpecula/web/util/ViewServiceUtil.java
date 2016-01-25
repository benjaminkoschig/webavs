package ch.globaz.vulpecula.web.util;

public class ViewServiceUtil {
    public static String returnNullIfJavascriptNull(String value) {
        if (value.equals("null")) {
            return null;
        }
        return value;
    }
}
