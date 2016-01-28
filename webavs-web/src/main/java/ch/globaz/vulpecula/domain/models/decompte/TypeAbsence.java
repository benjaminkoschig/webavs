package ch.globaz.vulpecula.domain.models.decompte;

/***
 * Enum�ration d�finissant les diff�rents types d'absence possibles pour un
 * d�compte
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public enum TypeAbsence {
    ABSENCES_JUSTIFIEES(68015001),
    ASSURANCE_MILITAIRE(68015002),
    CHOMAGE(68015003),
    CAISSE_ACCIDENTS_SUVA(68015004),
    MALADIE(68015005),
    SERVICE_MILITAIRE(68015006),
    VACANCES(68015007),
    MATERNITE(68015008),
    CONGES_NON_PAYES(68015009);

    private int value;

    private TypeAbsence(final int value) {
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
     * @param value
     *            String repr�sentant un code syt�me
     * @return {@link TypeAbsence} repr�sentant le type de salaire
     */
    public static TypeAbsence fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type d'absence");
        }

        for (TypeAbsence t : TypeAbsence.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type d'absence connu");
    }

    /**
     * Retourne si la code syst�me pass�e en param�tre correspondant bien � un {@link TypeAbsence}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            TypeAbsence.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
