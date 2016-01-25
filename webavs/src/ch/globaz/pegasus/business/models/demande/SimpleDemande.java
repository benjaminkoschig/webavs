/**
 * 
 */
package ch.globaz.pegasus.business.models.demande;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author ECO
 */
public class SimpleDemande extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatDemande = null;
    private String dateArrivee = null;
    private String dateDebut = null;
    private String dateDepot = null;
    private String dateFin = null;
    private String dateProchaineRevision = null;
    private String idDemande = null;
    private String idDossier = null;
    private String idGestionnaire = null;
    private String idInfoComplementaire = null;
    private Boolean isFratrie = false;
    private Boolean isPurRetro = false;
    private String motifProchaineRevision = null;
    private String typeDemande = null;

    /**
     * @return the csEtatDemande
     */
    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    /**
     * @return the dateArrivee
     */
    public String getDateArrivee() {
        return dateArrivee;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateDepot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the dateProchaineRevision
     */
    public String getDateProchaineRevision() {
        return dateProchaineRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDemande;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idGestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * @return the idInfoComplementaire
     */
    public String getIdInfoComplementaire() {
        return idInfoComplementaire;
    }

    public Boolean getIsFratrie() {
        return isFratrie;
    }

    public Boolean getIsPurRetro() {
        return isPurRetro;
    }

    public String getMotifProchaineRevision() {
        return motifProchaineRevision;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @param csEtatDemande
     *            the csEtatDemande to set
     */
    public void setCsEtatDemande(String csEtatDemande) {
        this.csEtatDemande = csEtatDemande;
    }

    /**
     * @param dateArrivee
     *            the dateArrivee to set
     */
    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param dateProchaineRevision
     *            the dateProchaineRevision to set
     */
    public void setDateProchaineRevision(String dateProchaineRevision) {
        this.dateProchaineRevision = dateProchaineRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDemande = id;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idGestionnaire
     *            the idGestionnaire to set
     */
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    /**
     * @param idInfoComplementaire
     *            the idInfoComplementaire to set
     */
    public void setIdInfoComplementaire(String idInfoComplementaire) {
        this.idInfoComplementaire = idInfoComplementaire;
    }

    public void setIsFratrie(Boolean isFratrie) {
        this.isFratrie = isFratrie;
    }

    public void setIsPurRetro(Boolean isPurRetro) {
        this.isPurRetro = isPurRetro;
    }

    public void setMotifProchaineRevision(String motifProchainneRevision) {
        motifProchaineRevision = motifProchainneRevision;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
