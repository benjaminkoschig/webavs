package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SuiviCaisseMaladieSimpleModel extends JadeSimpleModel {
    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    private static final long serialVersionUID = 507690924369064905L;

    private String id;
    private String idTravailleur;
    private String idCaisseMaladie;
    private String typeDocument;
    private String dateEnvoi;
    private Boolean isEnvoye;

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getIdCaisseMaladie() {
        return idCaisseMaladie;
    }

    public void setIdCaisseMaladie(String idCaisseMaladie) {
        this.idCaisseMaladie = idCaisseMaladie;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Boolean getIsEnvoye() {
        return isEnvoye;
    }

    public void setIsEnvoye(Boolean isEnvoye) {
        this.isEnvoye = isEnvoye;
    }

}
