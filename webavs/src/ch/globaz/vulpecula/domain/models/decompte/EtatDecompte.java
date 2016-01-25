package ch.globaz.vulpecula.domain.models.decompte;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant l'état d'un décompte défini dans le lot 6
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
 */
public enum EtatDecompte {
    GENERE(68012001),
    OUVERT(68012002),
    RECEPTIONNE(68012003),
    VALIDE(68012004),
    ERREUR(68012005),
    RECTIFIE(68012006),
    COMPTABILISE(68012007),
    ANNULE(68012008),
    TAXATION_DOFFICE(68012009),
    FACTURATION(68012010),
    SOMMATION(68012011);

    private int value;

    private EtatDecompte(final int value) {
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
     * @return Un état du {@link EtatDecompte}
     */
    public static EtatDecompte fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système d'etat de décompte");
        }

        for (EtatDecompte e : EtatDecompte.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun état de décompte connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link EtatDecompte}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            EtatDecompte.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (EtatDecompte t : EtatDecompte.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}
