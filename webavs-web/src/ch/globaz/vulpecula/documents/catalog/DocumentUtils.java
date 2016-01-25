package ch.globaz.vulpecula.documents.catalog;

import java.util.Map;

/**
 * Classe utilitaire pour les documents
 * 
 * @since WebBMS 0.01.03
 */
public final class DocumentUtils {

    /*
     * Avoid instantiation
     */
    private DocumentUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Formate le texte. Remplace un {key} par la valeur contenue dans la map
     * 
     * @param paragraphe : le message dans lequel se trouve les groupes à remplacer
     * @param parametresMap : les valeurs de remplacement
     * @return le message formatté
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static final String formatMessage(final String paragraphe, Map<String, String> parametresMap) {
        if (paragraphe == null || paragraphe.length() == 0) {
            return paragraphe;
        }

        // TODO Faire le test unitaire
        StringBuilder keyTemp = null;
        String temp = paragraphe;

        boolean flagKey = false;
        for (int i = 0; i < paragraphe.length(); i++) {
            if ('}' == paragraphe.charAt(i)) {
                flagKey = false;
                if (parametresMap.containsKey(keyTemp.toString())) {
                    String patternToReplace = '{' + keyTemp.toString() + '}';
                    temp = temp.replace(patternToReplace, parametresMap.get(keyTemp.toString()));
                }
            }

            if (flagKey) {
                keyTemp.append(paragraphe.charAt(i));
            }

            if ('{' == paragraphe.charAt(i)) {
                flagKey = true;
                keyTemp = new StringBuilder("");
            }
        }

        return temp;
    }

}
