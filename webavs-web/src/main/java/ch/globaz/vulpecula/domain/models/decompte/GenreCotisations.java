package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant le genre des cotisations du d�compte salaire
 * 
 * @author Jonas Paratte | Cr�� le 04.02.2016
 */
public enum GenreCotisations {
    AVS(68030001),
    AF(68030002),
    AF_AVS(68030003),
    SANS_AF_AVS(68030004);

    private int value;

    private GenreCotisations(final int value) {
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
     * @return Un �tat du {@link GenreCotisations}
     */
    public static GenreCotisations fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'etat de d�compte");
        }

        for (GenreCotisations e : GenreCotisations.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun �tat de d�compte connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link GenreCotisations}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            GenreCotisations.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (GenreCotisations t : GenreCotisations.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
