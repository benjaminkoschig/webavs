package ch.globaz.prestation.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum EtatPrestationAccordee {

    AJOURNE(52820005),
    CALCULE(52820001),
    DIMINUE(52820004),
    PARTIEL(52820003),
    VALIDE(52820002);

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
    public static EtatPrestationAccordee parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + EtatPrestationAccordee.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatPrestationAccordee.valueOf(intCodeSysteme);
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
