package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class SimpleOrdreVersementSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrestation;

    public SimpleOrdreVersementSearchModel() {
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
    public Class<? extends JadeAbstractModel> whichModelClass() {
        return SimpleOrdreVersement.class;
    }
}
