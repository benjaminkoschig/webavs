package ch.globaz.pegasus.rpc.business.models;

import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class SimpleAnnonceSearch extends DomaineJadeAbstractSearchModel {

    private String forIdLot;

    @Override
    public Class<SimpleAnnonce> whichModelClass() {
        return SimpleAnnonce.class;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public String getForIdLot() {
        return forIdLot;
    }
}
