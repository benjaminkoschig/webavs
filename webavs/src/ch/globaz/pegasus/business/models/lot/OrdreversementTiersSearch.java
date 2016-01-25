package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class OrdreversementTiersSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Collection<String> inIdsTiers;

    public Collection<String> getInIdsTiers() {
        return inIdsTiers;
    }

    public void setInIdsTiers(Collection<String> inIdsTiers) {
        this.inIdsTiers = inIdsTiers;
    }

    @Override
    public Class<OrdreversementTiers> whichModelClass() {
        return OrdreversementTiers.class;
    }

}
