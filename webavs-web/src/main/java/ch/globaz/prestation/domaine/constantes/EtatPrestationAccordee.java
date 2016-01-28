package ch.globaz.prestation.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum EtatPrestationAccordee {

    AJOURNE(52820005),
    CALCULE(52820001),
    DIMINUE(52820004),
    PARTIEL(52820003),
    VALIDE(52820002);

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
    public static EtatPrestationAccordee parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + EtatPrestationAccordee.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatPrestationAccordee.valueOf(intCodeSysteme);
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
    public static EtatPrestationAccordee valueOf(Integer codeSysteme) {
        if (codeSysteme != null) {
            for (EtatPrestationAccordee unTypeTraitementDecisionRente : EtatPrestationAccordee.values()) {
                if (unTypeTraitementDecisionRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeTraitementDecisionRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + EtatPrestationAccordee.class.getName() + "]");
    }

    private Integer codeSysteme;

    private EtatPrestationAccordee(Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
