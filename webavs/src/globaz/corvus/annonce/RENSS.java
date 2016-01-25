package globaz.corvus.annonce;

import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRStringFormatter;

/**
 * Permet de g�rer un NSS tout simplement avec des valeurs enti�res.
 * Les valeurs min et max de chaque position sont contr�l�s
 * Le pr�fixe '756' est automatiquement ins�r�
 * 
 * 
 * 756.1111.2222.33
 * -> 1111 : 1�re position
 * -> 2222 : 2�me position
 * -> 33 : 3�me position
 * 
 * @author lga
 * 
 */
public class RENSS {

    /**
     * Le pr�fix '756' utilis� par les NSS SOUS
     */
    public static final String NSS_PREFIX = "756";
    public static final String EMPTY_UNFORMATED_NSS_WITHOUT_PREFIX = "0000000000";
    public static final String EMPTY_FORMATED_NSS_WITHOUT_PREFIX = "0000.0000.00";
    public static final String EMPTY_UNFORMATED_NSS_WITH_PREFIX = NSS_PREFIX + EMPTY_UNFORMATED_NSS_WITHOUT_PREFIX;
    /**
     * Un NSS format� avec le pr�fixe 756 et tout des 0 : 756.0000.0000.00
     * Peu �tre utilis� par la m�thode statique <code>NSS.convertFormattedNSS(String nss)</code> pour g�n�rer un NSS � 0
     */
    public static final String EMPTY_FORMATED_NSS_WITH_PREFIX = NSS_PREFIX + "." + EMPTY_FORMATED_NSS_WITHOUT_PREFIX;

    /**
     * Pr�fixe d'un num�ro NSS. Toujours 756
     */
    private final int val0 = 756;

    /**
     * Premi�re composante du num�ro NSS.
     * 0000 � 9999
     */
    private int val1;

    /**
     * Deuxi�me composante du num�ro NSS.
     * 0000 � 9999
     */
    private int val2;

    /**
     * Troisi�me composante du num�ro NSS.
     * 00 � 99
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
            throw new IllegalArgumentException("The value [" + value + "] in position n�[" + position
                    + "] of a NSS number can not be bigger as [" + maxValue + "] for a NSS number");
        }
    }

    /**
     * Retourne le num�ro NSS non formatt�
     * 
     * @return le num�ro NSS non formatt� (ex : 7560132630265)
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
     * Retourne le num�ro NSS formatt�
     * 
     * @return le num�ro NSS formatt� (ex : 756.0132.6302.65)
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
     * Cr�er un NSS depuis une cha�ne de caract�re.
     * Le format attendu est le suivant 756.xxxx.xxxx.xx (ou x est un num�rique)
     * 
     * @param nss La chain de caract�re correspondant au NSS
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
