package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

public enum EtatCivil {
    CELIBATAIRE(515001),
    MARIE(515002),
    DIVORCE(515003),
    VEUF(515004),
    SEPARE(515005),
    SEPARE_DE_FAIT(515006),
    LPART(515007),
    LPART_DISSOUT(515008),
    LPART_DIS_DECES(515009),
    LPART_SEP_FAIT(515010),
    UNDEFINDED(0);

    private Integer codeSystem;

    EtatCivil(int codeSystem) {
        this.codeSystem = codeSystem;
    }

    public Integer getCodeSysteme() {
        return codeSystem;
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
    public static EtatCivil parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Sexe.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatCivil.valueOf(intCodeSysteme);
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
    public static EtatCivil valueOf(Integer codeSysteme) {
        if (codeSysteme != null) {
            for (EtatCivil etatCivil : EtatCivil.values()) {
                if (etatCivil.getCodeSysteme().equals(codeSysteme)) {
                    return etatCivil;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + EtatCivil.class.getName() + "]");
    }

    public static String getCodeFamille() {
        return "PYETATCIVI";
    }
}
