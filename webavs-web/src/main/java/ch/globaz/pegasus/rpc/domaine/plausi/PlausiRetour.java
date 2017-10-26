package ch.globaz.pegasus.rpc.domaine.plausi;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;

public class PlausiRetour {
    private List<RetourAnnonce> retours = new ArrayList<RetourAnnonce>();
    private String nss;
    private String idDecision;
    private Date receiptMonth;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public Date getReceiptMonth() {
        return receiptMonth;
    }

    public void setReceiptMonth(Date receiptMonth) {
        this.receiptMonth = receiptMonth;
    }

    public List<RetourAnnonce> getRetours() {
        return retours;
    }

    public void setRetours(List<RetourAnnonce> retours) {
        this.retours = retours;
    }

}
