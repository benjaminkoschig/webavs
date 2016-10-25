package globaz.cygnus.services.adaptationJournaliere;

import java.util.List;

public class RFQdRemovedPeriodeData {
    private String idQd;
    private List<String> idPeriodes;
    private RFAdaptationJournaliereContext context;

    public RFQdRemovedPeriodeData(final String idQd, final RFAdaptationJournaliereContext context,
            final List<String> idPeriodes) {
        this.idQd = idQd;
        this.context = context;
        this.idPeriodes = idPeriodes;
    }

    public String getIdQd() {
        return idQd;
    }

    public RFAdaptationJournaliereContext getContext() {
        return context;
    }

    public void setContext(RFAdaptationJournaliereContext context) {
        this.context = context;
    }

    public List<String> getIdPeriodes() {
        return idPeriodes;
    }
}
