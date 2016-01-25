package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import java.math.BigDecimal;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersement;

public class OrdreVersementPeriode {
    private OrdreVersement beneficiaire;
    private String idCompteAnnexe;
    private OrdreVersement restitution;

    public OrdreVersement getBeneficiaire() {
        return beneficiaire;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public BigDecimal getMontantBeneficiaire() {
        return resolveMontant(beneficiaire);
    }

    public OrdreVersement getRestitution() {
        return restitution;
    }

    public boolean hasDom2R() {
        return isBeneficiareDom2r() || isResitutionDom2r();
    }

    public boolean isBeneficiareDom2r() {
        return isDom2R(beneficiaire);
    }

    private boolean isDom2R(OrdreVersement ov) {
        if (ov == null) {
            return false;
        }
        return ov.isDom2R();
    }

    public boolean isResitutionDom2r() {
        return isDom2R(restitution);
    }

    private BigDecimal resolveMontant(OrdreVersement ov) {
        if (ov != null) {
            return ov.getMontant();
        } else {
            return new BigDecimal(0);
        }
    }

    public void setBeneficiaire(OrdreVersement beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setRestitution(OrdreVersement restitution) {
        this.restitution = restitution;
    }

}
