package ch.globaz.pegasus.business.constantes;

public enum EPCPlafondLoyer {
    PLAFOND_1_PERSONNE("64049010",1),
    PLAFOND_2_PERSONNES("64049011",2),
    PLAFOND_3_PERSONNES("64049012",3),
    PLAFOND_4_ET_PLUS("64049013",4),
    PLAFOND_COMMUNAUTE("64049014",0);

    private String code = null;
    private Integer nbPersonne;

    private EPCPlafondLoyer(String code, int nbPersonne) {
        this.code = code;
        this.nbPersonne = nbPersonne;
    }

    public static EPCPlafondLoyer getEnumByNbPersonne(Integer nbPersonne) {
        if (EPCPlafondLoyer.PLAFOND_1_PERSONNE.nbPersonne == nbPersonne) {
            return EPCPlafondLoyer.PLAFOND_1_PERSONNE;
        } else if (EPCPlafondLoyer.PLAFOND_2_PERSONNES.nbPersonne == nbPersonne) {
            return EPCPlafondLoyer.PLAFOND_2_PERSONNES;
        } else if (EPCPlafondLoyer.PLAFOND_3_PERSONNES.nbPersonne == nbPersonne) {
            return EPCPlafondLoyer.PLAFOND_3_PERSONNES;
        } else if (EPCPlafondLoyer.PLAFOND_4_ET_PLUS.nbPersonne <= nbPersonne) {
            return EPCPlafondLoyer.PLAFOND_4_ET_PLUS;
        } else if (EPCPlafondLoyer.PLAFOND_COMMUNAUTE.nbPersonne == nbPersonne) {
            return EPCPlafondLoyer.PLAFOND_COMMUNAUTE;
        }

        throw new IllegalArgumentException("No Enum specified for this nbPersonne " + nbPersonne);
    }

    public static EPCPlafondLoyer getEnumByCode(String code) {
        if (EPCPlafondLoyer.PLAFOND_1_PERSONNE.code.equals(code)) {
            return EPCPlafondLoyer.PLAFOND_1_PERSONNE;
        } else if (EPCPlafondLoyer.PLAFOND_2_PERSONNES.code.equals(code)) {
            return EPCPlafondLoyer.PLAFOND_2_PERSONNES;
        } else if (EPCPlafondLoyer.PLAFOND_3_PERSONNES.code.equals(code)) {
            return EPCPlafondLoyer.PLAFOND_3_PERSONNES;
        } else if (EPCPlafondLoyer.PLAFOND_4_ET_PLUS.code.equals(code)) {
            return EPCPlafondLoyer.PLAFOND_4_ET_PLUS;
        } else if (EPCPlafondLoyer.PLAFOND_COMMUNAUTE.code.equals(code)) {
            return EPCPlafondLoyer.PLAFOND_COMMUNAUTE;
        }
        throw new IllegalArgumentException("No Enum specified for this code " + code);
    }

    public String getCode() {
        return code;
    }

    public Integer getNbPersonne() {
        return nbPersonne;
    }

}
