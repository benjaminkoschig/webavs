package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypeDecisionRente {

    COMMUNICATION(52850001),
    DECISION(52850002),
    DECISION_SUR_OPPOSITION(52850003);

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
    public static TypeDecisionRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeDecisionRente.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeDecisionRente.valueOf(intCodeSysteme);
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
     * @return le code syst�me correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
