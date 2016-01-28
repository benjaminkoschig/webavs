package ch.globaz.vulpecula.domain.models.decompte;

/***
 * Enumération définissant les différents types d'absence possibles pour un
 * décompte
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
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
     * Retourne un code système représentant la valeur du type de salaire
     * 
     * @return String représentant un code sytème
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            String représentant un code sytème
     * @return {@link TypeAbsence} représentant le type de salaire
     */
    public static TypeAbsence fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type d'absence");
        }

        for (TypeAbsence t : TypeAbsence.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type d'absence connu");
    }

    /**
     * Retourne si la code système passée en paramètre correspondant bien à un {@link TypeAbsence}
     * 
     * @param value
     *            Code système
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
