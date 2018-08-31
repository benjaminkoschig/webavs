package ch.globaz.vulpecula.domain.models.postetravail;

public enum GenreModification {
    SANS_IMPACT_AF(0),
    DESACTIVATION(1),
    REACTIVATION(2),
    CHANGEMENT_FIN_POSTE(3);

    private int value;

    private GenreModification(final int value) {
        this.value = value;
    }

    /**
     * Construction de l'énumération à partir d'un code système
     * 
     * @param value
     *            Code système sous forme de String
     * @return Etat de l'énumération
     */
    public static GenreModification fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de GenreModifiation");
        }

        for (GenreModification gm : GenreModification.values()) {
            if (valueAsInt == gm.value) {
                return gm;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond à aucun GenreModifiation connue");
    }

    /**
     * Retourne le code système représentant la qualification
     * 
     * @return String représentant un code système
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Retourne si le paramètre correspond bien à un code système de la famille
     * des qualifications
     * 
     * @param value
     *            Code système
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
