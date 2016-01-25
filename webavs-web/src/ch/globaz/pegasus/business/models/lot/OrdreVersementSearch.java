package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class OrdreVersementSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDRE_BY_IDLOT_CS_TYPE = "csTypeOv";
    private String forIdLot = null;
    private String forIdPrestation = null;
    private String forIdVersionDroit = null;

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<OrdreVersement> whichModelClass() {
        return OrdreVersement.class;
    }

}
