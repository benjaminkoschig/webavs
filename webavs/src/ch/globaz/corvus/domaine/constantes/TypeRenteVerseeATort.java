package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypeRenteVerseeATort {

    AVANCE_DEJA_PERCUE(52861004),
    DETTES(52861001),
    PRESTATION_DEJA_VERSEE(52861007),
    PRESTATION_EN_SUSPENS(52861002),
    PRESTATION_NON_VERSEE(52861005),
    PRESTATION_TOUCHEE_INDUMENT(52861003),
    SAISIE_MANUELLE(52861006);

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
    public static TypeRenteVerseeATort parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeRenteVerseeATort.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeRenteVerseeATort.valueOf(intCodeSysteme);
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
    public static TypeRenteVerseeATort valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeRenteVerseeATort unTypeRenteVerseeATort : TypeRenteVerseeATort.values()) {
                if (unTypeRenteVerseeATort.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeRenteVerseeATort;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeRenteVerseeATort.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeRenteVerseeATort(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code syst�me correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
