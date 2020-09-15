package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet d'effectuer des recherches dans les assurances
 *
 * @author ESVE | Cr�� le 05 ao�t 2020
 *
 */
public class AssuranceSearchSimpleModel extends JadeSearchSimpleModel {

    private String forId;

    public String getForId() {
        return forId;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    @Override
    public Class<AssuranceSimpleModel> whichModelClass() {
        return AssuranceSimpleModel.class;
    }

}
