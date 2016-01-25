package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleTauxSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    @Override
    public Class whichModelClass() {
        return SimpleTaux.class;
    }

}
