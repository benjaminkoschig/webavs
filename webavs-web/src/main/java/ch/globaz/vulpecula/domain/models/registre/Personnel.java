package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant le type de personnel
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public enum Personnel {
    EXPLOITATION(68011001),
    ADMINISTRATIF(68011002);

    private int value;

    private Personnel(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de personnel
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
     * @return Un �tat du {@link Personnel}
     */
    public static Personnel fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur " + value
                    + " doit correspondre � un entier repr�sentant un code syst�me de personnel");
        }

        for (Personnel e : Personnel.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun personnel connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link Personnel}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            Personnel.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (Personnel t : Personnel.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}