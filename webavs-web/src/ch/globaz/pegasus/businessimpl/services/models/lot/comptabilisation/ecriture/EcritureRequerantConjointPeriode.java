package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class EcritureRequerantConjointPeriode {
    private EricturePeriode conjoint;
    private Integer noPeriode;
    private EricturePeriode requerant;

    public EcritureRequerantConjointPeriode() {
        requerant = new EricturePeriode();
        conjoint = new EricturePeriode();
    }

    public void addEcriture(Ecriture ecriture) throws ComptabiliserLotException {
        if (ecriture.isRequerant()) {
            addEcriturePeriode(ecriture, requerant);
        } else if (ecriture.isCojoint()) {
            addEcriturePeriode(ecriture, conjoint);
        } else {
            throw new ComptabiliserLotException("Unable to resolve for who is the ecriture " + toString());
        }
    }

    private void addEcriturePeriode(Ecriture ecriture, EricturePeriode ericturePeriode)
            throws ComptabiliserLotException {
        if (SectionPegasus.DECISION_PC.equals(ecriture.getSection())) {
            ericturePeriode.setBeneficiaire(ecriture);
        } else if (SectionPegasus.RESTIUTION.equals(ecriture.getSection())) {
            ericturePeriode.setRestitution(ecriture);
        } else {
            throw new ComptabiliserLotException("The section of this ecriutre is not treat " + toString());
        }
    }

    public EricturePeriode getConjoint() {
        return conjoint;
    }

    public Integer getNoPeriode() {
        return noPeriode;
    }

    public EricturePeriode getRequerant() {
        return requerant;
    }

    public boolean hasBeneficiaireConjoint() {
        return hasBeneficiare(conjoint);
    }

    public boolean hasBeneficiaireRequerant() {
        return hasBeneficiare(requerant);
    }

    private boolean hasBeneficiare(EricturePeriode ericturePeriode) {
        return (ericturePeriode != null) && (ericturePeriode.getBeneficiaire() != null);
    }

    public boolean isBeneficiaireDom2R() {
        return requerant.isBenficaireDom2R();
    }

    public void setConjoint(EricturePeriode conjoint) {
        this.conjoint = conjoint;
    }

    public void setNoPeriode(Integer noPeriode) {
        this.noPeriode = noPeriode;
    }

    public void setRequerant(EricturePeriode requerant) {
        this.requerant = requerant;
    }
}
