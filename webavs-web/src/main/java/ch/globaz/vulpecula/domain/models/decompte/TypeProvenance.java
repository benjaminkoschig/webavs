/**
 * 
 */
package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sel
 *
 */
public enum TypeProvenance {
	WEBMETIER_GENERE(0),
	WEBMETIER_MANUEL(68036001),
	EBUSINESS(68036002);
	
	private int value;
	 
    private TypeProvenance(final int value) {
        this.value = value;
    }
    
    /**
     * Retourne le code système
     *
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code système
     *
     * @param value
     *            String représentant un code système
     * @return Un état du {@link EtatDecompte}
     */
    public static TypeProvenance fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de provenance.");
        }

        for (TypeProvenance e : TypeProvenance.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de provenance.");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeProvenance}
     *
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
        	TypeProvenance.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeProvenance t : TypeProvenance.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

}
