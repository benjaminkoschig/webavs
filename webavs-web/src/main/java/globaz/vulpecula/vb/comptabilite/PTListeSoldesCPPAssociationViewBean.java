package globaz.vulpecula.vb.comptabilite;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTListeSoldesCPPAssociationViewBean extends PTListeProcessViewBean {

    private String dateUntil;
    private String orderBy;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDateUntil() {
        if (dateUntil == null) {
            return new Date().getSwissValue();
        }

        return dateUntil;
    }

    public void setDateUntil(String dateUntil) {
        this.dateUntil = dateUntil;
    }
}
