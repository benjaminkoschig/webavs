package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant le genre des cotisations du décompte salaire
 * 
 * @author Jonas Paratte | Créé le 04.02.2016
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
     * @return Un état du {@link GenreCotisations}
     */
    public static GenreCotisations fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'etat de décompte");
        }

        for (GenreCotisations e : GenreCotisations.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun état de décompte connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link GenreCotisations}
     * 
     * @param value
     *            Code système
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
