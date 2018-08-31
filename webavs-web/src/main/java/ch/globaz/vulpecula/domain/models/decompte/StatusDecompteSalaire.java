package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant l'état d'un décompte salaire défini pour eBusiness
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
     * Retourne le code système représentant le permis de travail
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
     * @return Un état du {@link StatusDecompteSalaire}
     */
    public static StatusDecompteSalaire fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'etat de décompte");
        }

        for (StatusDecompteSalaire e : StatusDecompteSalaire.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun état de décompte connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link StatusDecompteSalaire}
     *
     * @param value
     *            Code système
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
