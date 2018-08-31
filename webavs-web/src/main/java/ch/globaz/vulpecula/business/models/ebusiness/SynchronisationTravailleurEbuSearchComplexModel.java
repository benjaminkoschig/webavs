package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class SynchronisationTravailleurEbuSearchComplexModel extends JadeSearchComplexModel {

    private static final long serialVersionUID = -4511682102080653158L;

    private boolean forDateSynchronisationIsEmpty;
    private String forId;
    private String forIdTravailleur;
    private String forIdEmployeur;

    public SynchronisationTravailleurEbuSearchComplexModel() {
        super();
    }

    @Override
    public Class<SynchronisationTravailleurEbuComplexModel> whichModelClass() {
        return SynchronisationTravailleurEbuComplexModel.class;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
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

    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

}
