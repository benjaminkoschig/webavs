package ch.globaz.al.utils;

/**
 * Classe utilitaire pour remplacer les caract�res "pourris" en base de donn�es (caract�re de substition) par un
 * WILDCARD.
 * 
 * @author age
 * 
 */
public class ALEncodingUtils {
    private static final String WILDCARD_CHARACTER = "*";

    /**
     * Remplacer le caract�re de substition (0x1a) Unciode par le {@link ALEncodingUtils#WILDCARD_CHARACTER}
     * 
     * @param toConvert
     *            String � convertir
     * @return String sans le caract�re de substition
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
