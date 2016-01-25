package globaz.auriga.vb.sortiecap;

import globaz.globall.db.BSessionUtil;
import ch.globaz.auriga.business.models.ComplexSortieCAPDecisionCAPAffiliation;

public class AUSortieCapLineViewBean {
    private ComplexSortieCAPDecisionCAPAffiliation complexSortieCap = null;

    public AUSortieCapLineViewBean(ComplexSortieCAPDecisionCAPAffiliation sortieCap) {
        complexSortieCap = sortieCap;
    }

    public String getDateSortie() {
        return complexSortieCap.getSortieCap().getDateSortie();
    }

    public String getEtat() {
        return BSessionUtil.getSessionFromThreadContext().getCodeLibelle(complexSortieCap.getSortieCap().getEtat());
    }

    public String getIdPassageFacturation() {
        return complexSortieCap.getSortieCap().getIdPassageFacturation();
    }

    public String getIdSortie() {
        return complexSortieCap.getId();
    }

    public String getMontantExtourne() {
        return complexSortieCap.getSortieCap().getMontantExtourne();
    }

    public String getNumeroAffilie() {
        return complexSortieCap.getAffiliation().getAffilieNumero();
    }
}
