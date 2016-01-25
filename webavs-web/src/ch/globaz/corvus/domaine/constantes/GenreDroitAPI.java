package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * La façon dont le droit API est survenu
 */
public enum GenreDroitAPI {

    // @formatter:off
	API_AI_D_UNE_PERSONNE_AYANT_DROIT_A_UNE_PRESTATION(52809001),
	API_AI_D_UNE_PERSONNE_NE_BENEFICIANT_PAS_DE_RENTES(52809002),
	API_AVS_DONT_LE_DROIT_EST_NE_APRES_L_AGE_LEGAL_DE_LA_RETRAITE(52809004),
	API_AVS_QUI_SUCCEDE_A_UNE_API_AI(52809003);
	// @formatter:on

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un
     * valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static GenreDroitAPI parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + GenreDroitAPI.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return GenreDroitAPI.valueOf(intCodeSysteme);
    }

    /**
     * Retourne l'énuméré correspondant au code système.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du paramètre <code>codeSystem</code>
     * est null ou si le code système ne correspond pas à une valeur de cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static GenreDroitAPI valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (GenreDroitAPI unGenreDroitAPI : GenreDroitAPI.values()) {
                if (unGenreDroitAPI.getCodeSysteme().equals(codeSysteme)) {
                    return unGenreDroitAPI;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + GenreDroitAPI.class.getName() + "]");
    }

    private Integer codeSysteme;

    private GenreDroitAPI(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
