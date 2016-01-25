package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

class CompensationInfoRestiution extends MontantAdisposition<EricturePeriode> {
    private String idCompteAnnexe = null;
    private Map<EricturePeriode, BigDecimal> mapRestiutionNonRemboursee = new LinkedHashMap<EricturePeriode, BigDecimal>();

    protected void addMontantDispo(EricturePeriode ecriture) {
        this.addMontantDispo(ecriture.getMontantBeneficiaire());
    }

    public void addMontantDispo(EricturePeriode ericturePeriode, boolean isDom2R) {
        if (isDom2R) {
            this.addMontantDispoDom2R(ericturePeriode);
        } else {
            this.addMontantDispo(ericturePeriode);
        }
    }

    protected void addMontantDispoDom2R(EricturePeriode ecriture) {
        this.addMontantDispoDom2R(ecriture.getMontantBeneficiaire());
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public Map<EricturePeriode, BigDecimal> getMapRestiutionNonRemboursee() {
        return mapRestiutionNonRemboursee;
    }

    @Override
    public boolean hasMonney() {
        return getTotalMontantAdisposition().signum() == 1;
    }

    @Override
    public boolean hasMontantAdisposition() {
        return getMontantAdisposition().signum() == 1;
    }

    @Override
    public boolean hasMontantAdispositionDom2R() {
        return getMontantAdispositionDom2R().signum() == 1;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setMapRestiutionNonRemboursee(Map<EricturePeriode, BigDecimal> mapRestiutionNonRemboursee) {
        this.mapRestiutionNonRemboursee = mapRestiutionNonRemboursee;
    }
}
