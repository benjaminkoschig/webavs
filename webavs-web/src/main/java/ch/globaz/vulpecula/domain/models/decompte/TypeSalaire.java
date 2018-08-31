package ch.globaz.vulpecula.domain.models.decompte;

/***
 * Enum�ration d�finissant les diff�rents types de salaires possibles avec leurs codes
 * syst�mes associ�s.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public enum TypeSalaire {
    HEURES(68013001),
    MOIS(68013002),
    CONSTANT(68013003);

    private int value;

    private TypeSalaire(final int value) {
        this.value = value;
    }

    /**
     * Retourne un code syst�me repr�sentant la valeur du type de salaire
     * 
     * @return String repr�sentant un code syt�me
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'�num�ration � partir d'un code syst�me
     * 
     * @param value String repr�sentant un code syt�me
     * @return {@link TypeSalaire} repr�sentant le type de salaire
     */
    public static TypeSalaire fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type de salaire");
        }

        for (TypeSalaire t : TypeSalaire.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucune type de salaire connue");
    }

    /**
     * Retourne si la code syst�me pass�e en param�tre correspondant bien � un {@link TypeSalaire}
     * 
     * @param value Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            TypeSalaire.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
