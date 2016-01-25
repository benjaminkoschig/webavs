package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class SimpleSoldePourRestitutionSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdSoldePourRestitution;

    public SimpleSoldePourRestitutionSearchModel() {
        super();

        forIdSoldePourRestitution = null;
    }

    public String getForIdSoldePourRestitution() {
        return forIdSoldePourRestitution;
    }

    public void setForIdSoldePourRestitution(String forIdSoldePourRestitution) {
        this.forIdSoldePourRestitution = forIdSoldePourRestitution;
    }

    @Override
    public Class<? extends JadeAbstractModel> whichModelClass() {
        return SimpleSoldePourRestitution.class;
    }
}
