package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdreVersementWrapper {
    private List<OrdreVersementDisplay> ovs = new ArrayList<OrdreVersementDisplay>();
    private BigDecimal sum = new BigDecimal(0);

    public OrdreVersementWrapper() {

    }

    public OrdreVersementWrapper(List<OrdreVersementDisplay> ovs, BigDecimal sum) {
        super();
        this.ovs = ovs;
        this.sum = sum;
    }

    public void addAll(OrdreVersementWrapper ov) {
        sum = sum.add(ov.getSum());
        ovs.addAll(ov.getOvs());
    }

    public void addMontant(OrdreVersementDisplay ov, BigDecimal montant) {
        ov.addMontant(montant);
        sum = sum.add(montant);
    }

    public void addOv(OrdreVersementDisplay ov) {
        sum = sum.add(ov.getMontant());
        ovs.add(ov);
    }

    public List<OrdreVersementDisplay> getOvs() {
        return ovs;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void negateSum() {
        sum = sum.negate();
    }
}
