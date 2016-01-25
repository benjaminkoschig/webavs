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
    public static TypeRenteVerseeATort parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeRenteVerseeATort.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeRenteVerseeATort.valueOf(intCodeSysteme);
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
     * @return le code système correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
