package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;

class EricturePeriode {
    private Ecriture beneficiaire;
    private Ecriture restitution;

    public Ecriture getBeneficiaire() {
        return beneficiaire;
    }

    public BigDecimal getMontantBeneficiaire() {
        return beneficiaire.getMontant();
    }

    public BigDecimal getMontantRestitution() {
        return restitution.getMontant();
    }

    public Ecriture getRestitution() {
        return restitution;
    }

    public boolean isBenficaireDom2R() {
        return (beneficiaire != null) && beneficiaire.getOrdreVersement().isDom2R();

    }

    public BigDecimal resolveMontantMin() {
        return getMontantRestitution().min(getMontantBeneficiaire()).abs();
    }

    public void setBeneficiaire(Ecriture beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public void setRestitution(Ecriture restitution) {
        this.restitution = restitution;
    }

}
