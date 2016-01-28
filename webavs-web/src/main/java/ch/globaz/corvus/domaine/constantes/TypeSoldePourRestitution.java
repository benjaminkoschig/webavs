package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Énuméré représentant les différents types de soldes pour restitution
 */
public enum TypeSoldePourRestitution {

    EDITION_BVR(52851003),
    RESTITUTION(52851001),
    RETENUES(52851002);

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
    public static TypeSoldePourRestitution parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeSoldePourRestitution.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return TypeSoldePourRestitution.valueOf(intCodeSysteme);
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
     * @return le code système correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
