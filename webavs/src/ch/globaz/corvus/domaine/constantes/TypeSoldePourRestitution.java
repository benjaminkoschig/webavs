package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * �num�r� repr�sentant les diff�rents types de soldes pour restitution
 */
public enum TypeSoldePourRestitution {

    EDITION_BVR(52851003),
    RESTITUTION(52851001),
    RETENUES(52851002);

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
    public static TypeSoldePourRestitution parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeSoldePourRestitution.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeSoldePourRestitution.valueOf(intCodeSysteme);
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
    public static TypeSoldePourRestitution valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeSoldePourRestitution unTypeTraitementDecisionRente : TypeSoldePourRestitution.values()) {
                if (unTypeTraitementDecisionRente.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeTraitementDecisionRente;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeSoldePourRestitution.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeSoldePourRestitution(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code syst�me correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
