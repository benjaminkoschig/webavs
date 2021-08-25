package ch.globaz.common.acor;

class Slashs {
    static String addFirstSlash(final String path) {
        return path.startsWith("/") ? path : "/" + path;
    }

    static String deleteLastSlash(final String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }
}
