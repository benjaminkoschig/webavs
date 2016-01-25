package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Énuméré représentant les différents états d'une demande de rente, ainsi que les codes systèmes associés à ces états
 */
public enum EtatDemandeRente {

    // @formatter:off
	AU_CALCUL(52804002),
	CALCULE(52804003),
	COURANT_VALIDE(52804007),
	ENREGISTRE(52804001),
	TERMINE(52804008),
	TRANSFERE(52804006),
	VALIDE(52804004);
	// @formatter:on

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
    public static EtatDemandeRente parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + EtatDemandeRente.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatDemandeRente.valueOf(intCodeSysteme);
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
    public static EtatDemandeRente valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (EtatDemandeRente unEtat : EtatDemandeRente.values()) {
                if (unEtat.getCodeSysteme().equals(codeSysteme)) {
                    return unEtat;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + EtatDemandeRente.class.getName() + "]");
    }

    private Integer codeSysteme;

    private EtatDemandeRente(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
