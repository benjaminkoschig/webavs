package ch.globaz.common.util;

import globaz.commons.nss.NSUtil;
import org.apache.commons.lang3.StringUtils;

public interface NSSUtils {
    String AVS_CODE_PAYS = "756";

    static boolean checkNSS(String nss) {
        String unformatNss = NSUtil.unFormatAVS(nss);

        if (!(StringUtils.isNumeric(unformatNss) || nss.length() == 13)) {
            return false;
        }

        if (!StringUtils.startsWith(unformatNss, AVS_CODE_PAYS)) {
            return false;
        }

        int nbControle = 0;
        for (int i = 11; i > -1; i--) {
            if (i % 2 == 0) {
                nbControle = nbControle + Integer.parseInt(String.valueOf(unformatNss.charAt(i)));
            } else {
                nbControle = nbControle + Integer.parseInt(String.valueOf(unformatNss.charAt(i))) * 3;
            }
        }

        int numControle = 10 - nbControle % 10;

        return StringUtils.endsWith(unformatNss, String.valueOf(numControle));
    }
}
