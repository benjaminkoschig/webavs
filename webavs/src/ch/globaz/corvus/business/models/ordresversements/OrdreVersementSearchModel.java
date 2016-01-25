package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeComplexModel;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class OrdreVersementSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrestation;

    public OrdreVersementSearchModel() {
        super();

        forIdPrestation = null;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    @Override
    public Class<? extends JadeComplexModel> whichModelClass() {
        return OrdreVersementComplexModel.class;
    }
}
