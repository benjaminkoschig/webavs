package ch.globaz.corvus.business.models.ordresversements;

import globaz.globall.db.BConstants;
import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleOrdreVersement extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String _isCompensationInterDecision;
    private String _isCompense;
    private String _isValide;
    private String csRole;
    private String csType;
    private String idDomainApplication;
    private String idExterne;
    private String idOrdreVersement;
    private String idPrestation;
    private String idRenteAccordeeCompensee;
    private String idRenteAccordeeDiminuee;
    private String idRenteVerseeATort;
    private String idRoleCompteAnnexe;
    private String idTiers;
    private String idTiersAdressePaiement;
    private String montant;
    private String montantDette;
    private String noFacture;

    public SimpleOrdreVersement() {
        super();
    }

    public String get_isCompensationInterDecision() {
        return _isCompensationInterDecision;
    }

    public String get_isCompense() {
        return _isCompense;
    }

    public String get_isValide() {
        return _isValide;
    }

    public String getCsRole() {
        return csRole;
    }

    public String getCsType() {
        return csType;
    }

    @Override
    public String getId() {
        return idOrdreVersement;
    }

    public String getIdDomainApplication() {
        return idDomainApplication;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdRenteAccordeeCompensee() {
        return idRenteAccordeeCompensee;
    }

    public String getIdRenteAccordeeDiminuee() {
        return idRenteAccordeeDiminuee;
    }

    public String getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public String getIdRoleCompteAnnexe() {
        return idRoleCompteAnnexe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public Boolean getIsCompensationInterDecision() {
        return BConstants.DB_BOOLEAN_TRUE.equals(_isCompensationInterDecision);
    }

    public Boolean getIsCompense() {
        return BConstants.DB_BOOLEAN_TRUE.equals(_isCompense);
    }

    public Boolean getIsValide() {
        return BConstants.DB_BOOLEAN_TRUE.equals(_isValide);
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public String getNoFacture() {
        return noFacture;
    }

    public void set_isCompensationInterDecision(String isCompensationInterDecision) {
        _isCompensationInterDecision = isCompensationInterDecision;
    }

    public void set_isCompense(String isCompense) {
        _isCompense = isCompense;
    }

    public void set_isValide(String isValide) {
        _isValide = isValide;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    @Override
    public void setId(String id) {
        idOrdreVersement = id;
    }

    public void setIdDomainApplication(String idDomainApplication) {
        this.idDomainApplication = idDomainApplication;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdRenteAccordeeCompensee(String idRenteAccordeeCompensee) {
        this.idRenteAccordeeCompensee = idRenteAccordeeCompensee;
    }

    public void setIdRenteAccordeeDiminuee(String idRenteAccordeeDiminuee) {
        this.idRenteAccordeeDiminuee = idRenteAccordeeDiminuee;
    }

    public void setIdRenteVerseeATort(String idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdRoleCompteAnnexe(String idRoleCompteAnnexe) {
        this.idRoleCompteAnnexe = idRoleCompteAnnexe;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIsCompensationInterDecision(Boolean isCompensationInterDecision) {
        if ((isCompensationInterDecision != null) && isCompensationInterDecision) {
            _isCompensationInterDecision = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            _isCompensationInterDecision = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsCompense(Boolean isCompense) {
        if ((isCompense != null) && isCompense) {
            _isCompense = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            _isCompense = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsValide(Boolean isValide) {
        if ((isValide != null) && isValide) {
            _isValide = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            _isValide = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    public void setNoFacture(String noFacture) {
        this.noFacture = noFacture;
    }
}
