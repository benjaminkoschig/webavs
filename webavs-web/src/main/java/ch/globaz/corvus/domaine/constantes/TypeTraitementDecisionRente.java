package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Les différents type de décision de rentes
 */
public enum TypeTraitementDecisionRente {

    COURANT(52832002),
    RETRO(52832003),
    STANDARD(52832001);

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
    public static TypeTraitementDecisionRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeTraitementDecisionRente.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeTraitementDecisionRente.valueOf(intCodeSysteme);
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
    public static TypeTraitementDecisionRente valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeTraitementDecisionRente unTypeTraitementDecisionRente : TypeTraitementDecisionRente.values()) {
                if (unTypeTraitementDecisionRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeTraitementDecisionRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeTraitementDecisionRente.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeTraitementDecisionRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
    public String getCodeSystemAsString(){
        return String.valueOf(codeSysteme);
    }
}
