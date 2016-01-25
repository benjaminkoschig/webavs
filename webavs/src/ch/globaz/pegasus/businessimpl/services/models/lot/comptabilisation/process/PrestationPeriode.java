package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import java.math.BigDecimal;

public class PrestationPeriode {
    private OrdreVersementPeriode conjoint;
    private Integer noGroupePeriode;
    private OrdreVersementPeriode requerant;
    private BigDecimal sumConjoint = new BigDecimal(0);
    private BigDecimal sumRequerant = new BigDecimal(0);

    public OrdreVersementPeriode getConjoint() {
        return conjoint;
    }

    public Integer getNoGroupePeriode() {
        return noGroupePeriode;
    }

    public OrdreVersementPeriode getRequerant() {
        return requerant;
    }

    public BigDecimal getSumConjoint() {
        return sumConjoint;
    }

    public BigDecimal getSumRequerant() {
        return sumRequerant;
    }

    public void setConjoint(OrdreVersementPeriode conjoint) {
        this.conjoint = conjoint;
    }

    public void setNoGroupePeriode(Integer noGroupePeriode) {
        this.noGroupePeriode = noGroupePeriode;
    }

    public void setRequerant(OrdreVersementPeriode requerant) {
        this.requerant = requerant;
    }

    public void setSumConjoint(BigDecimal sumConjoint) {
        this.sumConjoint = sumConjoint;
    }

    public void setSumRequerant(BigDecimal sumRequerant) {
        this.sumRequerant = sumRequerant;
    }

    @Override
    public String toString() {
        return "PrestationPeriode [ noGroupePeriode=" + noGroupePeriode + ", conjoint=" + conjoint + ",requerant="
                + requerant + "]";
    }
}
