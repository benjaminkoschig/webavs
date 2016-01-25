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
     * Constructeur vide empêchant l'instanciation de l'objet
     */
    private TypesValidator() {

    }

    /**
     * Contrôle si la chaîne de caractères est de type numérique
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
