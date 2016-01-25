package globaz.ij.utils;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.math.BigInteger;

public class IJUtils {

    public static String getChiffreCleDecision(BSession session, String noAVS, String noOAI, String numeroDecision)
            throws Exception {

        BigDecimal onze = new BigDecimal(11);
        BigDecimal valeur = new BigDecimal(0);
        BigDecimal sommeValeur = new BigDecimal(0);
        String chaine = "";
        StringBuffer concatChaine = new StringBuffer();

        String numAVS = noAVS;
        int pos = 0;
        while ((pos = numAVS.indexOf(".")) > 0) {
            numAVS = numAVS.substring(0, pos) + numAVS.substring(pos + 1);
        }

        chaine = concatChaine.append(numAVS).append(noOAI).append(numeroDecision).toString();

        for (int i = 0; i < chaine.length(); i++) {
            valeur = new BigDecimal(chaine.substring(i, i + 1));
            valeur = valeur.multiply(new BigDecimal(i + 1));
            sommeValeur = sommeValeur.add(valeur);
        }

        BigInteger reste = sommeValeur.toBigInteger().mod(onze.toBigInteger());
        BigInteger cle = onze.toBigInteger().subtract(reste);

        if (cle.intValue() >= 10) {
            return "0";
        } else {
            return cle.toString();
        }
    }

    public static String getNumeroDecisionIJAIComplet(BSession session, String noOfficeAI, String nss, String noDecision) {

        if (JadeStringUtil.isBlankOrZero(noDecision)) {
            return "";
        }
        // Enlever les . du noAVS s'il y en a
        int pos = 0;
        while ((pos = nss.indexOf(".")) > 0) {
            nss = nss.substring(0, pos) + nss.substring(pos + 1);
        }

        // Créer le numéro complet avec ce format :

        // si ancien nss -> 00IIIAAAANNNNNNC+nss
        if (nss.length() == 11) {
            return "00" + noOfficeAI + noDecision + nss;
        }
        // si nouveau nss -> IIIAAAANNNNNNC+nss
        else if (nss.length() == 13) {
            return noOfficeAI + noDecision + nss;
        } else {
            return "";
        }
    }

}
