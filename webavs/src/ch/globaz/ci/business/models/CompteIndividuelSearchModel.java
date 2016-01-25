package ch.globaz.ci.business.models;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class CompteIndividuelSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNumAvsActuel = null;

    public String getForNumAvsActuel() {
        return forNumAvsActuel;
    }

    public void setForNumAvsActuel(String forNumAvsActuel) {
        this.forNumAvsActuel = JadeStringUtil.removeChar(forNumAvsActuel, '.');
    }

    @Override
    public Class whichModelClass() {
        return CompteIndividuelModel.class;
    }

}
