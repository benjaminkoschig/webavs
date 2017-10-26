package ch.globaz.common.persistence;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;
import java.util.HashSet;

public abstract class DomaineJadeAbstractSearchModel extends JadeSearchSimpleModel {
    private String forId;
    private Collection<String> forIds = new HashSet<String>();

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public Collection<String> getForIds() {
        return forIds;
    }

    public void setForIds(Collection<String> forIds) {
        this.forIds = forIds;
    }

}
