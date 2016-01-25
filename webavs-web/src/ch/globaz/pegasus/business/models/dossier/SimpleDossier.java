package ch.globaz.pegasus.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDossier extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemandePrestation = null;
    private String idDossier = null;
    private String idGestionnaire = null;

    @Override
    public String getId() {
        return idDossier;
    }

    /**
     * @return the idDemandePrestation
     */
    public String getIdDemandePrestation() {
        return idDemandePrestation;
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

    @Override
    public void setId(String id) {
        idDossier = id;
    }

    /**
     * @param idDemandePrestation
     *            the idDemandePrestation to set
     */
    public void setIdDemandePrestation(String idDemandePrestation) {
        this.idDemandePrestation = idDemandePrestation;
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

}
