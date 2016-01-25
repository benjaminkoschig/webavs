package ch.globaz.orion.business.models.pucs;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.Comparator;

public class PucsFileComparator implements Comparator<PucsFile> {
    BSession session = null;

    @Override
    public int compare(PucsFile o1, PucsFile o2) {
        try {
            if (BSessionUtil.compareDateFirstGreater(getSession(), o1.getDateDeReception(), o2.getDateDeReception())) {
                return -1;
            } else if (BSessionUtil.compareDateFirstLower(getSession(), o1.getDateDeReception(),
                    o2.getDateDeReception())) {
                return 1;
            } else {
                // Si égal, on compare sur le no d'affilié
                if (o1.getNumeroAffilie() != null && o2.getNumeroAffilie() != null) {
                    if (o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie()) == 0) {
                        return -1;
                    } else {
                        return o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie());
                    }
                }
                return -1;
            }

        } catch (Exception e) {
            return -1;
        }
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

}
