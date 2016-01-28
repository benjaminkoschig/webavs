package ch.globaz.amal.business.models.documents;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDocument extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEnvoi = null;
    private String idDetailEnvoiDocument = null;
    private String idDetailFamille = null;
    private String libelleEnvoi = null;
    private String numModele = null;

    /**
     * @return the dateEnvoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    @Override
    public String getId() {
        return idDetailEnvoiDocument;
    }

    /**
     * @return the idDetailEnvoiDocument
     */
    public String getIdDetailEnvoiDocument() {
        return idDetailEnvoiDocument;
    }

    /**
     * @return the idDetailFamille
     */
    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    /**
     * @return the libelleEnvoi
     */
    public String getLibelleEnvoi() {
        return libelleEnvoi;
    }

    /**
     * @return the numModele
     */
    public String getNumModele() {
        return numModele;
    }

    /**
     * @param dateEnvoi
     *            the dateEnvoi to set
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    @Override
    public void setId(String id) {
        idDetailEnvoiDocument = id;
    }

    /**
     * @param idDetailEnvoiDocument
     *            the idDetailEnvoiDocument to set
     */
    public void setIdDetailEnvoiDocument(String idDetailEnvoiDocument) {
        this.idDetailEnvoiDocument = idDetailEnvoiDocument;
    }

    /**
     * @param idDetailFamille
     *            the idDetailFamille to set
     */
    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    /**
     * @param libelleEnvoi
     *            the libelleEnvoi to set
     */
    public void setLibelleEnvoi(String libelleEnvoi) {
        this.libelleEnvoi = libelleEnvoi;
    }

    /**
     * @param numModele
     *            the numModele to set
     */
    public void setNumModele(String numModele) {
        this.numModele = numModele;
    }

}
