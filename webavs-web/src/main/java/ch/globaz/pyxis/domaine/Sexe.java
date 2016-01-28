package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Énuméré décrivant le sexe d'une personne dans le domaine des tiers
 */
public enum Sexe {

    FEMME(516002),
    HOMME(516001),
    UNDEFINDED(0);

    public static Sexe parseAllowEmpyOrZeroValue(String codeSysteme) {
        if (JadeStringUtil.isBlankOrZero(codeSysteme)) {
            return UNDEFINDED;
        }

        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Sexe.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Sexe.valueOf(intCodeSysteme);
    }

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
    public static Sexe parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Sexe.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Sexe.valueOf(intCodeSysteme);
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
    public static Sexe valueOf(Integer codeSysteme) {
        if (codeSysteme != null) {
            for (Sexe unSexe : Sexe.values()) {
                if (unSexe.getCodeSysteme().equals(codeSysteme)) {
                    return unSexe;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + Sexe.class.getName() + "]");
    }

    private Integer codeSysteme;

    private Sexe(Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code système correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
