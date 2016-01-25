package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Décrit les différentes façon dont un assuré peut anticiper sa rente vieillesse (lorsque la rente n'est pas anticipée,
 * l'état est {@link #AGE_LEGAL}
 */
public enum Anticipation {

    // @formatter:off
	AGE_LEGAL(52812001),
	DEUX_ANNEES(52812003),
	UNE_ANNEE(52812002);
	// @formatter:on

    /**
     * Retourne l'énuméré correspondant au code système. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * chaîne de
     * caractère passée en paramètre est invalide (null ou vide) ou si le code système ne correspond pas à un valeur de
     * cette énuméré.</strong>
     * 
     * @param codeSysteme la valeur du code système à rechercher
     * @throws IllegalArgumentException si le paramètre codeSystem est null, une chaîne vide ou ne correspond pas à une
     *             valeur connue
     * @return l'énuméré correspondant au code système. </br><strong>Si la valeur n'est pas trouvée une
     *         IllegalArgumentException sera lancée</strong>
     */
    public static Anticipation parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Anticipation.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Anticipation.valueOf(intCodeSysteme);
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
    public static Anticipation valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (Anticipation uneAnticipation : Anticipation.values()) {
                if (uneAnticipation.getCodeSysteme().equals(codeSysteme)) {
                    return uneAnticipation;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + Anticipation.class.getName() + "]");
    }

    private Integer codeSysteme;

    private Anticipation(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
