package ch.globaz.orion.utils;

import java.text.Normalizer;

public class StringConverter {
    /**
     * Transforme la chaine en majuscule. Converti les accents dans la lettre de base (� -> E, � -> E....) et supprime
     * les caract�res autres que "A-Z" "," "-" "'" et "espace"
     * 
     * @param src
     * @return
     */
    public static String normalizeChaine(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        String srcUncaccentUpper = toUpperWithoutAccent(src);
        return srcUncaccentUpper.replaceAll("[^A-Z,'\\-\\s]", "");
    }

    /**
     * Transforme la chaine en majuscule et converti les accents dans la lettre de base (� -> E, � -> E....)
     * 
     * @param src
     * @return
     */
    public static String toUpperWithoutAccent(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        String srcUnaccent = Normalizer.normalize(src, Normalizer.Form.NFD).replaceAll("\\p{IsM}", "");
        return srcUnaccent.toUpperCase();
    }

}
