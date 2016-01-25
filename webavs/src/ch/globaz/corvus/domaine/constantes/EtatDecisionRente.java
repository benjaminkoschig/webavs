package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Regroupe les différents états d'une décision de rente, sous la forme d'un énuméré. Il est possible d'accéder à la
 * valeur du code système par la méthode {@link #getCodeSysteme()} ou la valeur de l'énuméré pour un code système par la
 * méthode {@link #parse(String)}
 */
public enum EtatDecisionRente {

    // @formatter:off
	EN_ATTENTE(52837001),
	PRE_VALIDE(52837002),
	VALIDE(52837003);
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
    public static EtatDecisionRente parse(final String codeSysteme) {

        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + EtatDecisionRente.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatDecisionRente.valueOf(intCodeSysteme);
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
    public static EtatDecisionRente valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (EtatDecisionRente unTypeTraitementDecisionRente : EtatDecisionRente.values()) {
                if (unTypeTraitementDecisionRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeTraitementDecisionRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + EtatDecisionRente.class.getName() + "]");
    }

    private Integer codeSysteme;

    private EtatDecisionRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
