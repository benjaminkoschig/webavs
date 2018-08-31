package ch.globaz.vulpecula.domain.models.decompte;

public enum CodeErreur {
    REMARQUE(68035001),
    CODE_ABSENCE(68035002),
    NOUVELLE_LIGNE(68035003),
    SALAIRE_INFERIEUR_578(68035004),
    SALAIRE_SUPERIEUR_1763(68035005),
    SALAIRE_SUPERIEUR_12350(68035006),
    RETRAITE(68035007),
    NOUVEAU_TRAVAILLEUR(68035008),
    LIGNE_SUPPRIMEE(68035009),
    HORS_PERIODE(68035010),
    POST_IT(68035011),
    TAUX_DIFFERENT(68035012),
    SALAIRE_CHANGE(68035013),
    SALAIRE_SUPERIEUR_148200(68035014),
    SALAIRE_NEGATIF(68035015),
    SALAIRE_ZERO(68035016);

    private int value;

    private CodeErreur(final int value) {
        this.value = value;
    }

    /**
     * Retourne un code syst�me
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
     * @return {@link CodeErreurDecompteSalaire}
     */
    public static CodeErreur fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre � un entier repr�sentant un code syst�me de type code erreur");
        }

        for (CodeErreur t : CodeErreur.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond � aucun type de code erreur");
    }

    /**
     * Retourne si la code syst�me pass�e en param�tre correspondant bien � un {@link CodeErreurDecompteSalaire}
     * 
     * @param value
     *            Code syst�me
     * @return true si valide
     */
    public static boolean isValid(final String value) {
        try {
            CodeErreur.fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
