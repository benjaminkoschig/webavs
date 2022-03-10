package globaz.osiris.eservices.enums;

import globaz.osiris.external.IntRole;

public enum ESRole {
    ADMINISTRATEUR(IntRole.ROLE_ADMINISTRATEUR),
    ADMINISTRATION(IntRole.ROLE_ADMINISTRATION),
    AF(IntRole.ROLE_AF),
    AFFILIE(IntRole.ROLE_AFFILIE),
    AFFILIE_PARITAIRE(IntRole.ROLE_AFFILIE_PARITAIRE),
    AFFILIE_PERSONNEL(IntRole.ROLE_AFFILIE_PERSONNEL),
    AMC(IntRole.ROLE_AMC),
    APG(IntRole.ROLE_APG),
    ASSURE(IntRole.ROLE_ASSURE),
    BANQUE(IntRole.ROLE_BANQUE),
    CONTRIBUABLE(IntRole.ROLE_CONTRIBUABLE),
    DEBITEUR(IntRole.ROLE_DEBITEUR),
    FCF(IntRole.ROLE_FCF),
    IJAI(IntRole.ROLE_IJAI),
    PCF(IntRole.ROLE_PCF),
    RENTIER(IntRole.ROLE_RENTIER),
    BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES(IntRole.ROLE_BENEFICIAIRE_PRESTATIONS_CONVENTIONNELLES),
    ASSOCIATION_PROFESSIONNELLE(IntRole.ROLE_ASSOCIATION_PROFESSIONNELLE),
    ALL("");

    private String cs;

    ESRole(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return cs;
    }

}
