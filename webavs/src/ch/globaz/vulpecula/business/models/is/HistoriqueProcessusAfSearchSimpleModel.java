package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

public class HistoriqueProcessusAfSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 296951735253843848L;

    private String forId;
    private String forIdProcessus;
    private Collection<String> forIdsNotIn;

    @Override
    public Class<HistoriqueProcessusAfSimpleModel> whichModelClass() {
        return HistoriqueProcessusAfSimpleModel.class;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdProcessus() {
        return forIdProcessus;
    }

    public void setForIdProcessus(String forIdProcessus) {
        this.forIdProcessus = forIdProcessus;
    }

    public Collection<String> getForIdsNotIn() {
        return forIdsNotIn;
    }

    public void setForIdsNotIn(Collection<String> forIdsNotIn) {
        this.forIdsNotIn = forIdsNotIn;
    }
}
