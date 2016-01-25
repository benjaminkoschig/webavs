package ch.globaz.perseus.business.models.informationfacture;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleInformationFacture extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDossier = null;
    private String description = null;
    private String date = null;
    private String idInformationFacture = null;

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getId() {
        return idInformationFacture;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdInformationFacture() {
        return idInformationFacture;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(String idSimpleEcheanceLibre) {
        idInformationFacture = idSimpleEcheanceLibre;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdInformationFacture(String idInformationFacture) {
        this.idInformationFacture = idInformationFacture;
    }

}
