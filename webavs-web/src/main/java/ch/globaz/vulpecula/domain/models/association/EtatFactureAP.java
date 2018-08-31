package ch.globaz.vulpecula.domain.models.association;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant l'état de facturation des associations professionnelles
 * 
 * @author Paratte Jonas
 */
public enum EtatFactureAP {
    A_VALIDER(68033001),
    VALIDE(68033002),
    GENERE(68033003),
    COMPTABILISE(68033004),
    EN_ERREUR(68033005),
    SUPPRIME(68033006),
    REFUSE(68033007);

    private int value;

    private EtatFactureAP(final int value) {
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
     * @return Un état du {@link EtatFactureAP}
     */
    public static EtatFactureAP fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'etat de décompte");
        }

        for (EtatFactureAP e : EtatFactureAP.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun état de décompte connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link EtatFactureAP}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            EtatFactureAP.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatFactureAP t : EtatFactureAP.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
