package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypeDecisionRente {

    COMMUNICATION(52850001),
    DECISION(52850002),
    DECISION_SUR_OPPOSITION(52850003);

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
    public static TypeDecisionRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeDecisionRente.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeDecisionRente.valueOf(intCodeSysteme);
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
    public static TypeDecisionRente valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeDecisionRente unTypeDecisionRente : TypeDecisionRente.values()) {
                if (unTypeDecisionRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeDecisionRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeDecisionRente.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeDecisionRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
