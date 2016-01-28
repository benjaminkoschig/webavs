package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypeCalculDemandeRente {

    // @formatter:off
	BILATERALES(52803004),
	PAIEMENT_PROVISOIRE(52803002),
	PREVISIONNEL(52803003),
	STANDARD(52803001),
	TRANSITOIRE(52803005);
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
    public static TypeCalculDemandeRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeCalculDemandeRente.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeCalculDemandeRente.valueOf(intCodeSysteme);
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
    public static TypeCalculDemandeRente valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeCalculDemandeRente unTypeDeCalculDeDemandeRente : TypeCalculDemandeRente.values()) {
                if (unTypeDeCalculDeDemandeRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeDeCalculDeDemandeRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeCalculDemandeRente.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeCalculDemandeRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code syst�me correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
