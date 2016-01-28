package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

public enum TypeOrdreVersement {

    ALOCATION_DE_NOEL(52842012),
    ASSURANCE_SOCIALE(52842002),
    BENEFICIAIRE_PRINCIPAL(52842004),
    CREANCIER(52842001),
    DETTE(52842011),
    DETTE_RENTE_AVANCES(52842008),
    DETTE_RENTE_DECISION(52842005),
    DETTE_RENTE_PRST_BLOQUE(52842009),
    DETTE_RENTE_RESTITUTION(52842007),
    DETTE_RENTE_RETOUR(52842006),
    DIMINUTION_DE_RENTE(52842014),
    IMPOT_A_LA_SOURCE(52842003),
    INTERET_MORATOIRE(52842010),
    JOURS_APPOINT(52842013);

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
    public static TypeOrdreVersement parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + TypeOrdreVersement.class.getName() + "]");
        }
        return TypeOrdreVersement.valueOf(Integer.parseInt(codeSysteme));
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
    public static TypeOrdreVersement valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (TypeOrdreVersement unTypeOrdreVersement : TypeOrdreVersement.values()) {
                if (unTypeOrdreVersement.getCodeSysteme().equals(codeSysteme)) {
                    return unTypeOrdreVersement;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + TypeOrdreVersement.class.getName() + "]");
    }

    private Integer codeSysteme;

    private TypeOrdreVersement(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}