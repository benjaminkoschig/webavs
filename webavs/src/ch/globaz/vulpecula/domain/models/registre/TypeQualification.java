package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;

/***
 * Enumération représentant le type de qualification
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
 */
public enum TypeQualification {
    OUVRIER(68010001),
    EMPLOYE(68010002);

    private int value;

    private TypeQualification(int value) {
        this.value = value;
    }

    /**
     * Retourne le code système représentant le type de qualification
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
     * @return Un état du {@link TypeQualification}
     */
    public static TypeQualification fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de qualification");
        }

        for (TypeQualification e : TypeQualification.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond à aucun type de qualification connu");
    }

    /**
     * Retourne si le code système passée en paramètre correspondant bien à un {@link TypeQualification}
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
        for (TypeQualification t : TypeQualification.values()) {
            types.add(String.valueOf(t.value));
        }
        return types;
    }
}