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
     * Retourne un code système
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
     * @return {@link CodeErreurDecompteSalaire}
     */
    public static CodeErreur fromValue(final String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un code système de type code erreur");
        }

        for (CodeErreur t : CodeErreur.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun type de code erreur");
    }

    /**
     * Retourne si la code système passée en paramètre correspondant bien à un {@link CodeErreurDecompteSalaire}
     * 
     * @param value
     *            Code système
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
