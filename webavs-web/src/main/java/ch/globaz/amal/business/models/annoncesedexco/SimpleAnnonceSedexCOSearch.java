package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleAnnonceSedexCOSearch extends JadeSearchSimpleModel {

    @Override
    public Class whichModelClass() {
        return SimpleAnnonceSedexCO.class;
    }

}
