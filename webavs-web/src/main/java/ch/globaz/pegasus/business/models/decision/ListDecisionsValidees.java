package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

public class ListDecisionsValidees extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csMotifRefus = null;
    private String csMotifSuppression = null;
    private String csTypePCAccordee = null;
    private String dateDecision = null;
    private String dateValidation = null;
    private String idDecision = null;
    private String idTiers = null;
    private String nom = null;
    private String nss = null;
    private String prenom = null;
    private String typeDecision = null;
    private String validePar = null;
    private boolean decisionProvisoire = false;
    private String csEtat = null;

    public ListDecisionsValidees() {
        super();
    }

    public String getCsMotifRefus() {
        return csMotifRefus;
    }

    public String getCsMotifSuppression() {
        return csMotifSuppression;
    }

    public String getCsTypePCAccordee() {
        return csTypePCAccordee;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public String getValidePar() {
        return validePar;
    }

    public void setCsMotifRefus(String csMotifRefus) {
        this.csMotifRefus = csMotifRefus;
    }

    public void setCsMotifSuppression(String csMotifSuppression) {
        this.csMotifSuppression = csMotifSuppression;
    }

    public void setCsTypePCAccordee(String csTypePCAccordee) {
        this.csTypePCAccordee = csTypePCAccordee;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecision = id;

    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {

    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    public void setValidePar(String validePar) {
        this.validePar = validePar;
    }
    public boolean getDecisionProvisoire() {
        return decisionProvisoire;
    }

    public void setDecisionProvisoire(boolean decisionProvisoire) {
        this.decisionProvisoire = decisionProvisoire;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }


}
