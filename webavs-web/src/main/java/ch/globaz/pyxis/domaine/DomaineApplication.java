package ch.globaz.pyxis.domaine;

import globaz.jade.client.util.JadeStringUtil;

public enum DomaineApplication {
    COMPTE_INDIVIDUEL(519001),
    APG(519002),
    MATERNITE(519003),
    STANDARD(519004),
    COTISATIONS_PERSONNELLES(519005),
    PRESTATIONS(519006),
    DECLARATION_DE_SALAIRES(519008),
    IJAI(519009),
    REMBOURSEMENT_DE_COTISATION(519010),
    FACTURATION(519011),
    RECOUVREMENT_DRIECT_LSV_SDD(519012),
    CONTENTIEUX(519013),
    SURSIS_PAIEMENT(519014),
    PATERNITE(519017),
    PROCHE_AIDANT(519018);

    private Integer systemCode;

    DomaineApplication(int systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getSystemCode() {
        return systemCode;
    }

    public static DomaineApplication parse(String systemCode) throws IllegalArgumentException {
        if (!JadeStringUtil.isDigit(systemCode)) {
            throw new IllegalArgumentException("The value [" + systemCode
                    + "] is not valid for the systemCode of type [" + DomaineApplication.class.getName() + "]");
        }

        Integer intSystemCode = Integer.parseInt(systemCode);
        return DomaineApplication.valueOf(intSystemCode);
    }

    public static DomaineApplication valueOf(Integer systemCode) throws IllegalArgumentException {
        if (systemCode != null) {
            for (DomaineApplication domaineApplication : DomaineApplication.values()) {
                if (domaineApplication.getSystemCode().equals(systemCode)) {
                    return domaineApplication;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + systemCode + "] is not valid for the systemCode of type ["
                + DomaineApplication.class.getName() + "]");
    }
}
