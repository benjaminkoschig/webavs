package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant le type de paramètre de calcul des frais d'association pour une cotisation
 * 
 * @author CEL
 */
public enum TypeParamCotisationAP {
    MONTANT_MIN(68031001),
    MONTANT_MAX(68031002),
    FORFAIT_FIX(68031005, 1),
    TAUX_FIX(68031003, 2),
    TAUX_CUMULATIF(68031004, 3),
    RABAIS(68031006, 4),
    FORFAIT_COTISATION(68031007),
    FACTEUR(68031008);

    private int value;
    private int priority;

    private TypeParamCotisationAP(int value) {
        this.value = value;
        priority = 0;
    }

    /**
     * Constructeur qui prend la priorité en paramètre, ce qui permet le tri dans l'écran et le document
     * 
     * @param value
     * @param priority
     */
    private TypeParamCotisationAP(int value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    /**
     * Retourne le code système représentant le type de paramètre cotisation A.P.
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    public String getPriority() {
        return String.valueOf(priority);
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            String représentant un code système
     * @return Un état du {@link TypeParamCotisationAP}
     */
    public static TypeParamCotisationAP fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de paramètre cotisation A.P.");
        }

        for (TypeParamCotisationAP e : TypeParamCotisationAP.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond à aucun type de paramètre cotisation A.P. connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeParamCotisationAP}
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(String value) {
        try {
            TypeQualification.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static List<String> getList() {
        List<String> types = new ArrayList<String>();
        for (TypeParamCotisationAP t : TypeParamCotisationAP.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}