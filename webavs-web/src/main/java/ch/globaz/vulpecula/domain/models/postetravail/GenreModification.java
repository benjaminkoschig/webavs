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
     * Construction de l'�num�ration � partir d'un code syst�me
     * 
     * @param value
     *            Code syst�me sous forme de String
     * @return Etat de l'�num�ration
     */
    public static GenreModification fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de GenreModifiation");
        }

        for (GenreModification gm : GenreModification.values()) {
            if (valueAsInt == gm.value) {
                return gm;
            }
        }
        throw new IllegalArgumentException("La valeur (" + value + ") ne correspond � aucun GenreModifiation connue");
    }

    /**
     * Retourne le code syst�me repr�sentant la qualification
     * 
     * @return String repr�sentant un code syst�me
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Retourne si le param�tre correspond bien � un code syst�me de la famille
     * des qualifications
     * 
     * @param value
     *            Code syst�me
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
