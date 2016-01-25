package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;

class MontantDispo {
    BigDecimal dom2RConjoint = new BigDecimal(0);
    BigDecimal dom2RRequerant = new BigDecimal(0);
    BigDecimal standardConjoint = new BigDecimal(0);
    BigDecimal standardRequerant = new BigDecimal(0);

    public MontantDispo(BigDecimal dom2rRequerant, BigDecimal dom2rConjoint, BigDecimal standardRequerant,
            BigDecimal standardConjoint) {
        dom2RRequerant = dom2rRequerant;
        dom2RConjoint = dom2rConjoint;
        this.standardRequerant = standardRequerant;
        this.standardConjoint = standardConjoint;
    }

    public MontantDispo(int dom2rRequerant, int dom2rConjoint, int standardRequerant, int standardConjoint) {
        dom2RRequerant = new BigDecimal(dom2rRequerant);
        dom2RConjoint = new BigDecimal(dom2rConjoint);
        this.standardRequerant = new BigDecimal(standardRequerant);
        this.standardConjoint = new BigDecimal(standardConjoint);
    }

    public BigDecimal getAllMontantConjoint() {
        return dom2RConjoint.add(standardConjoint);
    }

    public BigDecimal getAllMontantRequerant() {
        return dom2RRequerant.add(standardRequerant);
    }

    public BigDecimal getDom2RConjoint() {
        return dom2RConjoint;
    }

    public BigDecimal getDom2RRequerant() {
        return dom2RRequerant;
    }

    public BigDecimal getStandarConjoint() {
        return standardConjoint;
    }

    public BigDecimal getStandardRequerant() {
        return standardRequerant;
    }

    public boolean hasMoney() {
        return getAllMontantConjoint().add(getAllMontantRequerant()).signum() == 1;
    }

    public boolean hasMontantDispoConjont() {
        return getAllMontantConjoint().signum() == 1;
    }

    public boolean hasMontantDispoDom2RConjoint() {
        return dom2RConjoint.signum() == 1;
    }

    public boolean hasMontantDispoDom2RRequerant() {
        return dom2RRequerant.signum() == 1;
    }

    public boolean hasMontantDispoRequerant() {
        return getAllMontantRequerant().signum() == 1;
    }

    public boolean hasMontantStandardDisoConjoint() {
        return standardConjoint.signum() == 1;
    }

    public boolean hasMontantStandardDispoRequerant() {
        return standardRequerant.signum() == 1;
    }

}