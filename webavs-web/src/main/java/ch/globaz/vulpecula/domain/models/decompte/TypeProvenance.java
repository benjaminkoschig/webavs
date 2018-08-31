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
     * Retourne le code syst�me
     *
     * @return String repr�sentant un code syst�me
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'�num�ration � partir d'un code syst�me
     *
     * @param value
     *            String repr�sentant un code syst�me
     * @return Un �tat du {@link EtatDecompte}
     */
    public static TypeProvenance fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de provenance.");
        }

        for (TypeProvenance e : TypeProvenance.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type de provenance.");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeProvenance}
     *
     * @param value
     *            Code syst�me
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
