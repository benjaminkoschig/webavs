package ch.globaz.common.util;

import org.apache.commons.lang3.StringUtils;

public final class NSSUtils {
    private NSSUtils(){}
    public static final String AVS_CODE_PAYS = "756";

    public static boolean checkNSS(String nss) {
        String unformatedNss = unFormatNss(nss);

        if (!StringUtils.isNumeric(unformatedNss) || unformatedNss.length() != 13) {
            return false;
        }

        if (!StringUtils.startsWith(unformatedNss, AVS_CODE_PAYS)) {
            return false;
        }

        int nbControle = 0;
        for (int i = 11; i > -1; i--) {
            if (i % 2 == 0) {
                nbControle = nbControle + Integer.parseInt(String.valueOf(unformatedNss.charAt(i)));
            } else {
                nbControle = nbControle + Integer.parseInt(String.valueOf(unformatedNss.charAt(i))) * 3;
            }
        }

        int numControle = nbControle % 10 == 0 ? 0 : 10 - nbControle % 10;

        return StringUtils.endsWith(unformatedNss, String.valueOf(numControle));
    }

    public static String unFormatNss(String nss) {
        if (StringUtils.isEmpty(nss)) {throw new IllegalArgumentException();}
        return nss.replaceAll("\\.", "");
    }

    public static String formatNss(String nss) {
        if (StringUtils.isEmpty(nss) || !checkNSS(nss)) {throw new IllegalArgumentException();}

        return unFormatNss(nss).replaceAll("^(\\d{3})(\\d{4})(\\d{4})(\\d{2})$", "$1.$2.$3.$4");
    }
}
