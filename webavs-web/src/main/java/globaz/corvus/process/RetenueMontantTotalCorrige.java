package globaz.corvus.process;

class RetenueMontantTotalCorrige {

    private String section;
    private String idExterneRole;
    private String montantTotalARetenir;
    private String montantTotaleAretenirCorriger;
    private String designation;
    private String idTiers;
    private String idCompteAnnexe;
    private String nss;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public String getMontantTotalARetenir() {
        return montantTotalARetenir;
    }

    public void setMontantTotalARetenir(String montantTotalARetenir) {
        this.montantTotalARetenir = montantTotalARetenir;
    }

    public String getMontantTotaleAretenirCorriger() {
        return montantTotaleAretenirCorriger;
    }

    public void setMontantTotaleAretenirCorriger(String montantTotaleAretenirCorriger) {
        this.montantTotaleAretenirCorriger = montantTotaleAretenirCorriger;
    }

    @Override
    public String toString() {
        return "RetenueMontantTotalCorrige [section=" + section + ", idExterneRole=" + idExterneRole
                + ", montantTotalARetenir=" + montantTotalARetenir + ", montantTotaleAretenirCorriger="
                + montantTotaleAretenirCorriger + ", designation=" + designation + ", idTiers=" + idTiers
                + ", idCompteAnnexe=" + idCompteAnnexe + ", nss=" + nss + "]";
    }

}
