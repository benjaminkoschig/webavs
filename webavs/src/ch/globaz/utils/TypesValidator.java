/**
 * 
 */
package ch.globaz.utils;

/**
 * Validation des types
 * 
 * 
 */
public final class TypesValidator {

    /**
     * Constructeur vide emp�chant l'instanciation de l'objet
     */
    private TypesValidator() {

    }

    /**
     * Contr�le si la cha�ne de caract�res est de type num�rique
     * 
     * @param value Une valeur
     * @return true si la valeur est numeric
     */
    public static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
