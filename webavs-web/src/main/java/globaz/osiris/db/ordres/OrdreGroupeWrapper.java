package globaz.osiris.db.ordres;

import java.util.ArrayList;
import java.util.List;

public class OrdreGroupeWrapper {
    private CAOrdreGroupe ordreGroupe;
    private List<String> reasons;

    public OrdreGroupeWrapper(CAOrdreGroupe og) {
        ordreGroupe = og;
        reasons = new ArrayList<String>();
    }

    public CAOrdreGroupe getOrdreGroupe() {
        return ordreGroupe;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void addReason(final String reason) {
        reasons.add(reason);
    }

    public void addAllReason(List<String> reasons) {
        this.reasons.addAll(reasons);
    }
}
