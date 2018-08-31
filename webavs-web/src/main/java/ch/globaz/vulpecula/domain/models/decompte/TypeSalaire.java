package ch.globaz.vulpecula.domain.models.decompte;

/***
 * Enumération définissant les différents types de salaires possibles avec leurs codes
 * systèmes associés.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
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
     * @param value String représentant un code sytème
     * @return {@link TypeSalaire} représentant le type de salaire
     */
    public static TypeSalaire fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type de salaire");
        }

        for (TypeSalaire t : TypeSalaire.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucune type de salaire connue");
    }

    /**
     * Retourne si la code système passée en paramètre correspondant bien à un {@link TypeSalaire}
     * 
     * @param value Code système
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
