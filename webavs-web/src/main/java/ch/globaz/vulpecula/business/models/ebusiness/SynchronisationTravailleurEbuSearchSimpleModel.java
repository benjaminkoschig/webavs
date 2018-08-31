package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SynchronisationTravailleurEbuSearchSimpleModel extends JadeSearchSimpleModel {

    private static final long serialVersionUID = 2809198692738776500L;

    private boolean forDateSynchronisationIsEmpty;
    private String forId;
    private String forIdTravailleur;
    private String forIdAnnonce;
    private String forCorrelationId;
    private String forPosteCorrelationId;

    @Override
    public Class<SynchronisationTravailleurEbuSimpleModel> whichModelClass() {
        return SynchronisationTravailleurEbuSimpleModel.class;
    }

    public boolean isForDateSynchronisationIsEmpty() {
        return forDateSynchronisationIsEmpty;
    }

    public void setForDateSynchronisationIsEmpty(boolean forDateSynchronisationIsEmpty) {
        this.forDateSynchronisationIsEmpty = forDateSynchronisationIsEmpty;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public String getForCorrelationId() {
        return forCorrelationId;
    }

    public void setForCorrelationId(String forCorrelationId) {
        this.forCorrelationId = forCorrelationId;
    }

    public String getForPosteCorrelationId() {
        return forPosteCorrelationId;
    }

    public void setForPosteCorrelationId(String forPosteCorrelationId) {
        this.forPosteCorrelationId = forPosteCorrelationId;
    }

}
