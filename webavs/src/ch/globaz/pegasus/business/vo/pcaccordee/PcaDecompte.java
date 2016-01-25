package ch.globaz.pegasus.business.vo.pcaccordee;

import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class PcaDecompte {
    private String idCompteAnnexe = null;
    private String idCompteAnnexeConjoint = null;
    private String idTiersAdressePaiement = null;
    private String idTiersAdressePaiementConjoint;
    private String idTiersBeneficiaire = null;
    private String idTiersConjoint = null;
    private String montantPCMensuelle = null;
    private SimplePCAccordee simplePCAccordee = null;
    private String sousCodePresation = null;

    public PcaDecompte() {
        super();
        simplePCAccordee = new SimplePCAccordee();
    }

    public String getCsEtatPC() {
        return simplePCAccordee.getCsEtatPC();
    }

    public String getCsGenrePC() {
        return simplePCAccordee.getCsGenrePC();
    }

    public String getCsRoleBeneficiaire() {
        return simplePCAccordee.getCsRoleBeneficiaire();
    }

    public String getCsTypePC() {
        return simplePCAccordee.getCsTypePC();
    }

    public String getDateDebut() {
        return simplePCAccordee.getDateDebut();
    }

    public String getDateFin() {
        return simplePCAccordee.getDateFin();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteAnnexeConjoint() {
        return idCompteAnnexeConjoint;
    }

    public String getIdPCAccordee() {
        return simplePCAccordee.getIdPCAccordee();
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersAdressePaiementConjoint() {
        return idTiersAdressePaiementConjoint;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getIdVersionDroit() {
        return simplePCAccordee.getIdVersionDroit();
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public String getSousCodePresation() {
        return sousCodePresation;
    }

    public void setCsEtatPC(String csEtatPC) {
        simplePCAccordee.setCsEtatPC(csEtatPC);
    }

    public void setCsGenrePC(String csGenrePC) {
        simplePCAccordee.setCsGenrePC(csGenrePC);
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        simplePCAccordee.setCsRoleBeneficiaire(csRoleBeneficiaire);
    }

    public void setCsTypePC(String csTypePC) {
        simplePCAccordee.setCsTypePC(csTypePC);
    }

    public void setDateDebut(String dateDebut) {
        simplePCAccordee.setDateDebut(dateDebut);
    }

    public void setDateFin(String dateFin) {
        simplePCAccordee.setDateFin(dateFin);
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdCompteAnnexeConjoint(String idCompteAnnexeConjoint) {
        this.idCompteAnnexeConjoint = idCompteAnnexeConjoint;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        simplePCAccordee.setIdPCAccordee(idPCAccordee);
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiementConjoint(String idTiersAdressePaiementConjoint) {
        this.idTiersAdressePaiementConjoint = idTiersAdressePaiementConjoint;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSousCodePresation(String sousCodePresation) {
        this.sousCodePresation = sousCodePresation;
    }

    @Override
    public String toString() {
        return "CalculRetro [ dateDebut=" + simplePCAccordee.getDateDebut() + ", dateFin="
                + simplePCAccordee.getDateFin() + ", montantPCMensuelle=" + montantPCMensuelle + ", idPca="
                + simplePCAccordee.getIdPCAccordee() + ", sousCodePresation=" + sousCodePresation + "]";
    }

}
