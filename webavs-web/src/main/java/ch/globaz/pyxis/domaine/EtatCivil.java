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
    public static EtatCivil parse(String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Sexe.class.getName() + "]");
        }

        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return EtatCivil.valueOf(intCodeSysteme);
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
