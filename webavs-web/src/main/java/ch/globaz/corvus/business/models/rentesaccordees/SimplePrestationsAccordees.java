package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.globall.db.BConstants;
import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePrestationsAccordees extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String _isAttenteMajBlocage = null;
    private String _isAttenteMajRetenue = null;
    private String _isErreur = null;
    private String _isPrestationBloquee = null;
    private String _isRetenues = null;
    private String codePrestation = null;
    private String csEtat = null;
    private String csGenre = null;
    private String dateDebutDroit = null;
    private String dateEcheance = null;
    private String dateFinDroit = null;
    private String fractionRente = null;
    private String idCalculInteretMoratoire = null;
    private String idDemandePrincipaleAnnulante = null;
    private String idEnteteBlocage = null;
    private String idInfoCompta = null;
    private String idPrestationAccordee = null;
    private String idTiersBeneficiaire = null;
    private String montantPrestation = null;
    private String referencePmt = null;
    private String sousCodePrestation = null;
    private String typeDeMiseAJours = null;

    public String get_isAttenteMajBlocage() {
        return _isAttenteMajBlocage;
    }

    public String get_isAttenteMajRetenue() {
        return _isAttenteMajRetenue;
    }

    public String get_isErreur() {
        return _isErreur;
    }

    public String get_isPrestationBloquee() {
        return _isPrestationBloquee;
    }

    public String get_isRetenues() {
        return _isRetenues;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenre() {
        return csGenre;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    @Override
    public String getId() {
        return idPrestationAccordee;
    }

    public String getIdCalculInteretMoratoire() {
        return idCalculInteretMoratoire;
    }

    public String getIdDemandePrincipaleAnnulante() {
        return idDemandePrincipaleAnnulante;
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsAttenteMajBlocage() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isAttenteMajBlocage));
    }

    public Boolean getIsAttenteMajRetenue() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isAttenteMajRetenue));
    }

    public Boolean getIsErreur() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isErreur));
    }

    public Boolean getIsPrestationBloquee() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isPrestationBloquee));
    }

    public Boolean getIsRetenues() {
        return new Boolean(BConstants.DB_BOOLEAN_TRUE.equals(_isRetenues));
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getReferencePmt() {
        return referencePmt;
    }

    public String getSousCodePrestation() {
        return sousCodePrestation;
    }

    public String getTypeDeMiseAJours() {
        return typeDeMiseAJours;
    }

    public void set_isAttenteMajBlocage(String _isAttenteMajBlocage) {
        this._isAttenteMajBlocage = _isAttenteMajBlocage;
    }

    public void set_isAttenteMajRetenue(String _isAttenteMajRetenue) {
        this._isAttenteMajRetenue = _isAttenteMajRetenue;
    }

    public void set_isErreur(String _isErreur) {
        this._isErreur = _isErreur;
    }

    public void set_isPrestationBloquee(String _isPrestationBloquee) {
        this._isPrestationBloquee = _isPrestationBloquee;
    }

    public void set_isRetenues(String _isRetenues) {
        this._isRetenues = _isRetenues;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    @Override
    public void setId(String id) {
        idPrestationAccordee = id;
    }

    public void setIdCalculInteretMoratoire(String idCalculInteretMoratoire) {
        this.idCalculInteretMoratoire = idCalculInteretMoratoire;
    }

    public void setIdDemandePrincipaleAnnulante(String idDemandePrincipaleAnnulante) {
        this.idDemandePrincipaleAnnulante = idDemandePrincipaleAnnulante;
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsAttenteMajBlocage(Boolean _isAttenteMajBlocage) {
        if (_isAttenteMajBlocage.booleanValue()) {
            this._isAttenteMajBlocage = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            this._isAttenteMajBlocage = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsAttenteMajRetenue(Boolean _isAttenteMajRetenue) {
        if (_isAttenteMajRetenue.booleanValue()) {
            this._isAttenteMajRetenue = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            this._isAttenteMajRetenue = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsErreur(Boolean _isErreur) {
        if (_isErreur.booleanValue()) {
            this._isErreur = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            this._isErreur = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsPrestationBloquee(Boolean _isPrestationBloquee) {
        if (_isPrestationBloquee.booleanValue()) {
            this._isPrestationBloquee = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            this._isPrestationBloquee = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setIsRetenues(Boolean _isRetenues) {
        if (_isRetenues.booleanValue()) {
            this._isRetenues = BConstants.DB_BOOLEAN_TRUE.toString();
        } else {
            this._isRetenues = BConstants.DB_BOOLEAN_FALSE.toString();
        }
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setReferencePmt(String referencePmt) {
        this.referencePmt = referencePmt;
    }

    public void setSousCodePrestation(String sousCodePrestation) {
        this.sousCodePrestation = sousCodePrestation;
    }

    public void setTypeDeMiseAJours(String typeDeMiseAJours) {
        this.typeDeMiseAJours = typeDeMiseAJours;
    }
}
