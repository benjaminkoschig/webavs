package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * La fa�on dont le droit API est survenu
 */
public enum GenreDroitAPI {

    // @formatter:off
	API_AI_D_UNE_PERSONNE_AYANT_DROIT_A_UNE_PRESTATION(52809001),
	API_AI_D_UNE_PERSONNE_NE_BENEFICIANT_PAS_DE_RENTES(52809002),
	API_AVS_DONT_LE_DROIT_EST_NE_APRES_L_AGE_LEGAL_DE_LA_RETRAITE(52809004),
	API_AVS_QUI_SUCCEDE_A_UNE_API_AI(52809003);
	// @formatter:on

    /**
     * Retourne l'�num�r� correspondant au code syst�me. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * cha�ne de caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un
     * valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, une cha�ne vide ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
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
     * Retourne l'�num�r� correspondant au code syst�me.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du param�tre <code>codeSystem</code>
     * est null ou si le code syst�me ne correspond pas � une valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
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
     * @return le code syst�me correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
