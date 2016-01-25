package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

public abstract class OrdreVersementDisplay {

    private String csTypeOv;
    private boolean isRequerant = false;
    private BigDecimal montant;
    private Integer noPeriode;

    public OrdreVersementDisplay(String csTypeOv, BigDecimal montant) {
        super();
        this.csTypeOv = csTypeOv;
        this.montant = montant;
    }

    public OrdreVersementDisplay(String csTypeOv, BigDecimal montant, String noPeriode, boolean isRequerant) {
        super();
        this.csTypeOv = csTypeOv;
        this.montant = montant;
        this.noPeriode = JadeStringUtil.isBlank(noPeriode) ? 0 : Integer.valueOf(noPeriode);
        this.isRequerant = isRequerant;
    }

    protected void addMontant(BigDecimal montant) {
        this.montant = this.montant.add(montant);
    }

    public String getCsTypeOv() {
        return csTypeOv;
    }

    public abstract String getDescriptionOv();

    public abstract String getId();

    public BigDecimal getMontant() {
        return montant;
    }

    public Integer getNoPeriode() {
        return noPeriode;
    }

    public boolean isRequerant() {
        return isRequerant;
    }

}
