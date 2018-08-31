package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SynchronisationTravailleurEbuSimpleModel extends JadeSimpleModel {

    private static final long serialVersionUID = -1163315186553118777L;

    private String id;
    private String idTravailleur;
    private String dateAjout;
    private String dateSynchronisation;
    private String correlationId;
    private String posteCorrelationId;
    private String idAnnonce;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getDateSynchronisation() {
        return dateSynchronisation;
    }

    public void setDateSynchronisation(String dateSynchronisation) {
        this.dateSynchronisation = dateSynchronisation;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

}
