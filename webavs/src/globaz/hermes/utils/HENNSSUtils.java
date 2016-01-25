package globaz.hermes.utils;

import globaz.commons.nss.NSUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.jade.client.util.JadeStringUtil;

public class HENNSSUtils {
    public final static String CST_NNSS = "756";
    public final static String PARAM_NNSS = "NNSS";

    public static String afficheNumAVS(String idChamp, String value) {
        if (IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS.equals(idChamp)) {
            return value;
        } else {
            if (HENNSSUtils.isNNSSNegatif(value)) {
                return convertNegatifToNNSS(value);
            } else {
                return value;
            }
        }
    }

    public static String convertNegatifToNNSS(String numeroAVS) {
        if (JadeStringUtil.isBlank(numeroAVS) || !numeroAVS.startsWith("-")) {
            return NSUtil.unFormatAVS(numeroAVS);
        } else {
            return NSUtil.unFormatAVS(CST_NNSS + JadeStringUtil.substring(numeroAVS, 1));
        }

    }

    public static String convertNNSStoNegatif(String numeroAVS) {
        if (JadeStringUtil.isBlank(numeroAVS)) {
            return numeroAVS;
        } else {
            return "-" + JadeStringUtil.substring(JadeStringUtil.removeChar(numeroAVS, '.'), 3);
        }
    }

    public static boolean isNNSS(String isNNSS) {
        return "true".equalsIgnoreCase(isNNSS);
    }

    public static String isNNSSFromNumAVS(String numeroAVS) {
        if (JadeStringUtil.isBlank(numeroAVS)) {
            return "";
        } else {
            return numeroAVS.startsWith("-") ? "true" : "false";
        }
    }

    public static boolean isNNSSLength(String nss) {
        if (!JadeStringUtil.isBlank(nss)) {
            if (NSUtil.unFormatAVS(nss).trim().length() == 13) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNNSSNegatif(String numeroAVS) {
        return JadeStringUtil.startsWith(numeroAVS, "-");
    }
}
