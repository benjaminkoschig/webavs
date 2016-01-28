package ch.globaz.pegasus.business.models.dettecomptatcompense;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DetteCompenseOrdreVersementSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrestation = null;
    private String forIdVersionDroit = null;
    private boolean forIsCompense = false;

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public boolean getForIsCompense() {
        return forIsCompense;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForIsCompense(boolean forIsCompense) {
        this.forIsCompense = forIsCompense;
    }

    @Override
    public Class<DetteCompenseOrdreVersement> whichModelClass() {
        return DetteCompenseOrdreVersement.class;
    }

}
