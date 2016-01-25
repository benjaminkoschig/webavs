package ch.globaz.vulpecula.external.models.hercule;

import java.util.ArrayList;
import java.util.List;

public enum InteretsMoratoires {
    AUTOMATIQUE(229001),
    SOUMIS(229002),
    EXEMPTE(229003),
    MANUEL(229004),
    A_CONTROLER(229005);

    private int value;

    private InteretsMoratoires(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le permis de travail
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
     * @return Un type de d�compte {@link InteretsMoratoires}
     */
    public static InteretsMoratoires fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'interet moratoire.");
        }

        for (InteretsMoratoires t : InteretsMoratoires.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun int�ret moratoire");
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (InteretsMoratoires t : InteretsMoratoires.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }

    /**
     * Retourne si la valeur pass�e en param�tre est un code syst�me valide
     * repr�sentant un {@link InteretsMoratoires}.
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            InteretsMoratoires.fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
