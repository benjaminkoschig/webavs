package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypePrestationDue {

    MONTANT_RETROACTIF_TOTAL(52828002),
    PAIEMENT_MENSUEL(52828001),
    RENCHERISSEMENT(52829002);

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
    public static TypePrestationDue parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypePrestationDue.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypePrestationDue.valueOf(intCodeSysteme);
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
    public static TypePrestationDue valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypePrestationDue unTypePrestationDue : TypePrestationDue.values()) {
                if (unTypePrestationDue.getCodeSysteme().equals(codeSysteme)) {
                    return unTypePrestationDue;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypePrestationDue.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypePrestationDue(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code syst�me correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
