package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

class MontantAdisposition<T> {
    private Map<T, BigDecimal> mapMontantNonRemboursee = new LinkedHashMap<T, BigDecimal>();
    private BigDecimal montantAdisposition = new BigDecimal(0);
    private BigDecimal montantAdispositionDom2R = new BigDecimal(0);

    protected void addMontantDispo(BigDecimal montan) {
        this.montantAdisposition = this.getMontantAdisposition().add(montan);
    }

    protected void addMontantDispoDom2R(BigDecimal montant) {
        this.montantAdispositionDom2R = this.getMontantAdispositionDom2R().add(montant);
    }

    public Map<T, BigDecimal> getMapMontantNonRemboursee() {
        return this.mapMontantNonRemboursee;
    }

    public BigDecimal getMontantAdisposition() {
        return this.montantAdisposition;
    }

    public BigDecimal getMontantAdispositionDom2R() {
        return this.montantAdispositionDom2R;
    }

    public BigDecimal getTotalMontantAdisposition() {
        return this.montantAdisposition.add(this.montantAdispositionDom2R);
    }

    public boolean hasMonney() {
        return this.getTotalMontantAdisposition().signum() == 1;
    }

    public boolean hasMontantAdisposition() {
        return this.getMontantAdisposition().signum() == 1;
    }

    public boolean hasMontantAdispositionDom2R() {
        return this.getMontantAdispositionDom2R().signum() == 1;
    }

    public void setMapMontantNonRemboursee(Map<T, BigDecimal> mapMontantNonRemboursee) {
        this.mapMontantNonRemboursee = mapMontantNonRemboursee;
    }

    public void substractMontantAdisposition(BigDecimal montant) {
        this.montantAdisposition = this.montantAdisposition.subtract(montant);
    }

    public void substractMontantAdispositionDom2R(BigDecimal montant) {
        this.montantAdispositionDom2R = this.montantAdispositionDom2R.subtract(montant);
    }
}
