package globaz.corvus.annonce;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRStringFormatter;

/**
 * Permet de gérer un NSS tout simplement avec des valeurs entières.
 * Les valeurs min et max de chaque position sont contrôlés
 * Le préfixe '756' est automatiquement inséré
 * 
 * 
 * 756.1111.2222.33
 * -> 1111 : 1ère position
 * -> 2222 : 2ème position
 * -> 33 : 3ème position
 * 
 * @author lga
 * 
 */
public class RENSS {

    /**
     * Le préfix '756' utilisé par les NSS SOUS
     */
    public static final String NSS_PREFIX = "756";
    public static final String EMPTY_UNFORMATED_NSS_WITHOUT_PREFIX = "0000000000";
    public static final String EMPTY_FORMATED_NSS_WITHOUT_PREFIX = "0000.0000.00";
    public static final String EMPTY_UNFORMATED_NSS_WITH_PREFIX = NSS_PREFIX + EMPTY_UNFORMATED_NSS_WITHOUT_PREFIX;
    /**
     * Un NSS formaté avec le préfixe 756 et tout des 0 : 756.0000.0000.00
     * Peu être utilisé par la méthode statique <code>NSS.convertFormattedNSS(String nss)</code> pour générer un NSS à 0
     */
    public static final String EMPTY_FORMATED_NSS_WITH_PREFIX = NSS_PREFIX + "." + EMPTY_FORMATED_NSS_WITHOUT_PREFIX;

    /**
     * Préfixe d'un numéro NSS. Toujours 756
     */
    private final int val0 = 756;

    /**
     * Première composante du numéro NSS.
     * 0000 à 9999
     */
    private int val1;

    /**
     * Deuxième composante du numéro NSS.
     * 0000 à 9999
     */
    private int val2;

    /**
     * Troisième composante du numéro NSS.
     * 00 à 99
     */
    private int val3;

    public RENSS(int val1, int val2, int val3) throws IllegalArgumentException {
        validateValue(val1, 9999, 1);
        validateValue(val2, 9999, 2);
        validateValue(val3, 99, 3);

        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    private void validateValue(int value, int maxValue, int position) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException("The value [" + value + "] can not be smaller as 0 for a NSS number");
        }
        if (value > maxValue) {
            throw new IllegalArgumentException("The value [" + value + "] in position n°[" + position
                    + "] of a NSS number can not be bigger as [" + maxValue + "] for a NSS number");
        }
    }

    /**
     * Retourne le numéro NSS non formatté
     * 
     * @return le numéro NSS non formatté (ex : 7560132630265)
     */
    public String getUnformatedNSS() {
        StringBuilder sb = new StringBuilder();
        sb.append(val0);
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val1), 4, "0"));
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val2), 4, "0"));
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val3), 2, "0"));
        return sb.toString();
    }

    /**
     * Retourne le numéro NSS formatté
     * 
     * @return le numéro NSS formatté (ex : 756.0132.6302.65)
     */
    public String getFormatedNSS() {
        StringBuilder sb = new StringBuilder();
        sb.append(val0);
        sb.append(".");
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val1), 4, "0"));
        sb.append(".");
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val2), 4, "0"));
        sb.append(".");
        sb.append(PRStringFormatter.indentLeft(String.valueOf(val3), 2, "0"));
        return sb.toString();
    }

    /**
     * Créer un NSS depuis une chaîne de caractère.
     * Le format attendu est le suivant 756.xxxx.xxxx.xx (ou x est un numérique)
     * 
     * @param nss La chain de caractère correspondant au NSS
     * @return
     * @throws REIllegalNSSFormatException
     */
    public static RENSS convertFormattedNSS(String nss) throws REIllegalNSSFormatException {
        RENSS result = null;
        if (JadeStringUtil.isEmpty(nss)) {
            throw new REIllegalNSSFormatException("nss is null or empty");
        }
        if (nss.length() != 16) {
            throw new REIllegalNSSFormatException("Invalid NSS format [" + nss
                    + "]. Must match format 756.xxxx.xxxx.xx");
        }

        String[] values = nss.split("\\.");
        if (values.length != 4) {
            throw new REIllegalNSSFormatException("Invalid NSS format [" + nss
                    + "]. Must match format 756.xxxx.xxxx.xx");
        }
        if (!"756".equals(values[0])) {
            throw new REIllegalNSSFormatException("Invalid NSS format [" + nss
                    + "]. Must match format 756.xxxx.xxxx.xx");
        }
        result = new RENSS(Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3]));

        return result;
    }
}
