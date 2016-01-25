package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant les motifs de prolongation d'un décompte
 * 
 * @author Paratte Jonas (JPA) | Créé le 20 06 2014
 */
public enum MotifProlongation {
    SANS(0),
    TELEPHONE(68018001),
    CONFIRMATION_PAIEMENT(68018002),
    SURSIS_PAIEMENT(68018003),
    AUTRE(68018004);

    private int value;

    private MotifProlongation(final int value) {
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
     * @return Un état du {@link MotifProlongation}
     */
    public static MotifProlongation fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de motif de prolongation");
        }

        for (MotifProlongation e : MotifProlongation.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun motif de prolongation connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link MotifProlongation}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            MotifProlongation.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (MotifProlongation t : MotifProlongation.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
