package globaz.aries.vb.sortiecgas;

import globaz.globall.db.BSessionUtil;
import ch.globaz.aries.business.models.ComplexSortieCGASDecisionCGASAffiliation;

public class ARSortieCgasLineViewBean {
    private ComplexSortieCGASDecisionCGASAffiliation complexSortieCgas = null;

    public ARSortieCgasLineViewBean(ComplexSortieCGASDecisionCGASAffiliation sortieCgas) {
        complexSortieCgas = sortieCgas;
    }

    public String getDateSortie() {
        return complexSortieCgas.getSortieCgas().getDateSortie();
    }

    public String getEtat() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(complexSortieCgas.getSortieCgas().getEtat());
    }

    public String getIdPassageFacturation() {
        return complexSortieCgas.getSortieCgas().getIdPassageFacturation();
    }

    public String getIdSortie() {
        return complexSortieCgas.getId();
    }

    public String getMontantExtourne() {
        return complexSortieCgas.getSortieCgas().getMontantExtourne();
    }

    public String getNumeroAffilie() {
        return complexSortieCgas.getAffiliation().getAffilieNumero();
    }
}
