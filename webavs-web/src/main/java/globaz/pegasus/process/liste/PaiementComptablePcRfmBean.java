package globaz.pegasus.process.liste;

import ch.globaz.common.domaine.Montant;

public class PaiementComptablePcRfmBean {

    public String idTiers;
    public Montant montant;
    public String codeDebitCredit;
    public String referenceRubrique;

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getReferenceRubrique() {
        return referenceRubrique;
    }

    public void setReferenceRubrique(String referenceRubrique) {
        this.referenceRubrique = referenceRubrique;
    }
}
