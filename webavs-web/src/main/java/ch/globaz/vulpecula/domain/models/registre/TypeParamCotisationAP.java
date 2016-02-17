package ch.globaz.vulpecula.domain.models.registre;

import java.util.ArrayList;
import java.util.List;

/***
 * Enum�ration repr�sentant le type de param�tre de calcul des frais d'association pour une cotisation
 * 
 * @author CEL
 */
public enum TypeParamCotisationAP {
    MONTANT_MIN(68030001),
    MONTANT_MAX(68030002),
    TAUX_FIX(68030003),
    TAUX_CUMULATIF(68030004),
    FORFAIT_FIX(68030005),
    FORFAIT_CUMULATIF(68030006),
    RABAIS(68030007);

    private int value;

    private TypeParamCotisationAP(int value) {
        this.value = value;
    }

    /**
     * Retourne le code syst�me repr�sentant le type de param�tre cotisation A.P.
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
     * @return Un �tat du {@link TypeParamCotisationAP}
     */
    public static TypeParamCotisationAP fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de param�tre cotisation A.P.");
        }

        for (TypeParamCotisationAP e : TypeParamCotisationAP.values()) {
            if (valueAsInt == e.value) {
                return e;
            }
        }
        throw new IllegalArgumentException("La valeur : " + value
                + " ne correspond � aucun type de param�tre cotisation A.P. connu");
    }

    /**
     * Retourne si le code syst�me pass�e en param�tre correspondant bien � un {@link TypeParamCotisationAP}
     * 
     * @param value
     *            Code syst�me
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