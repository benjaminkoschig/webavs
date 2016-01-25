package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

/**
 * �num�r� d�crivant le sexe d'une personne dans le domaine des tiers
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
    public static Sexe parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Sexe.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Sexe.valueOf(intCodeSysteme);
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
     * @return le code syst�me correspondant, sous forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
