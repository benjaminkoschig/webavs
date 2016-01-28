package ch.globaz.al.utils;

/**
 * Classe utilitaire pour remplacer les caractères "pourris" en base de données (caractère de substition) par un
 * WILDCARD.
 * 
 * @author age
 * 
 */
public class ALEncodingUtils {
    private static final String WILDCARD_CHARACTER = "*";

    /**
     * Remplacer le caractère de substition (0x1a) Unciode par le {@link ALEncodingUtils#WILDCARD_CHARACTER}
     * 
     * @param toConvert
     *            String à convertir
     * @return String sans le caractère de substition
     */
    public static String checkAndReplaceByWilcard(String toConvert) {
        StringBuilder sb = new StringBuilder();
        for (char c : toConvert.toCharArray()) {
            if (c == (char) 0x1a) {
                sb.append(ALEncodingUtils.WILDCARD_CHARACTER);
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
