package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant l'�tat d'un d�compte salaire d�fini pour eBusiness
 *
 */
// TODO ENUM A SUPPRIMER
public enum StatusDecompteSalaire {
//    A_SUPPRIMER(68035001),
	STANDARD(0),
    A_TRAITER(68035002);

    private int value;

    private StatusDecompteSalaire(final int value) {
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
     * @return Un �tat du {@link StatusDecompteSalaire}
     */
    public static StatusDecompteSalaire fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me d'etat de d�compte");
        }

        for (StatusDecompteSalaire e : StatusDecompteSalaire.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun �tat de d�compte connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link StatusDecompteSalaire}
     *
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            StatusDecompteSalaire.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (StatusDecompteSalaire t : StatusDecompteSalaire.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
