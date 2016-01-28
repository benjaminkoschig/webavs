package ch.globaz.perseus.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * TODO Javadoc métier ;-)
 * 
 * @author vyj
 */
public class SimpleDossier extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annoncesChangements = null;
    private String dateRevision = null;
    private String gestionnaire = null;
    private String idDemandePrestation = null;
    private String idDossier = null;

    /**
     * @return the annoncesChangements
     */
    public String getAnnoncesChangements() {
        return annoncesChangements;
    }

    /**
     * @return the dateRevision
     */
    public String getDateRevision() {
        return dateRevision;
    }

    /**
     * @return alias du gestionnaire
     */
    public String getGestionnaire() {
        return gestionnaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDossier;
    }

    /**
     * @return
     */
    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    /**
     * @return
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @param annoncesChangements
     *            the annoncesChangements to set
     */
    public void setAnnoncesChangements(String annoncesChangements) {
        this.annoncesChangements = annoncesChangements;
    }

    /**
     * @param dateRevision
     *            the dateRevision to set
     */
    public void setDateRevision(String dateRevision) {
        this.dateRevision = dateRevision;
    }

    /**
     * @param gestionnaire
     */
    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDossier = id;
    }

    /**
     * @param idDemandePrestation
     */
    public void setIdDemandePrestation(String idDemandePrestation) {
        this.idDemandePrestation = idDemandePrestation;
    }

    /**
     * @param idDossier
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

}
