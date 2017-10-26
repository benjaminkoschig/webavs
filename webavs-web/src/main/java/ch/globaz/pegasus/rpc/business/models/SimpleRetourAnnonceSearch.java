package ch.globaz.pegasus.rpc.business.models;

import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class SimpleRetourAnnonceSearch extends DomaineJadeAbstractSearchModel {

    @Override
    public Class<SimpleRetourAnnonce> whichModelClass() {
        return SimpleRetourAnnonce.class;
    }
}
