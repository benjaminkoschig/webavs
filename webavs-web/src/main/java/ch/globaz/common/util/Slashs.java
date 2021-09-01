package ch.globaz.common.util;

public class Slashs {
    public  static String addFirstSlash(final String path) {
        return path.startsWith("/") ? path : "/" + path;
    }

    public static String addLastSlash(final String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    public static String deleteLastSlash(final String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }
}
