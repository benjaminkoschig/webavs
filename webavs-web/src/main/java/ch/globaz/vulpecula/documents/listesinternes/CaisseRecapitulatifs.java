package ch.globaz.vulpecula.documents.listesinternes;

import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;

public class CaisseRecapitulatifs {
    private Taux taux;
    private Montant contribution = Montant.ZERO;
    private Montant masse = Montant.ZERO;

    public Montant getContributions() {
        return contribution;
    }

    public Montant getMasse() {
        return masse;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    public Taux getTaux() {
        return taux;
    }

    public CaisseRecapitulatifs addContribution(Montant contribution) {
        this.contribution = this.contribution.add(contribution);
        return this;
    }

    public CaisseRecapitulatifs substractContribution(Montant contribution) {
        this.contribution = this.contribution.substract(contribution);
        return this;
    }

    public CaisseRecapitulatifs addMasse(Montant masse) {
        this.masse = this.masse.add(masse);
        return this;
    }

    public CaisseRecapitulatifs substractMasse(Montant masse) {
        this.masse = this.masse.substract(masse);
        return this;
    }
}
