package globaz.naos.util;

import globaz.jade.client.util.JadeStringUtil;

public class IDEUtils {
    public static final String getPrefixeIDE() {
        return "CHE";
    }

    public static final String removePrefixeFromNumeroIDE(String numeroIDE) {
        if (JadeStringUtil.isBlank(numeroIDE)) {
            return numeroIDE;
        }

        if (numeroIDE.toUpperCase().startsWith("CHE")) {
            return numeroIDE.substring(3);
        }

        return numeroIDE;
    }
}