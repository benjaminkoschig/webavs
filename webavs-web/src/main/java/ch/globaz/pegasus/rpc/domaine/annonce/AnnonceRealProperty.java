package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceRealProperty {

    protected Montant realProperty;
    protected Montant mortgageDebts;
    protected Montant propertyIncome;
    protected Montant mortgageInterest;
    protected Montant maintenanceFees;
    protected Montant interestFeesEligible;

    public AnnonceRealProperty(RpcDecisionAnnonceComplete annonce) {
        realProperty = annonce.getRpcCalcul().getFortuneImmobiliere();
        mortgageDebts = annonce.getRpcCalcul().getDettesHypothequaires();
        propertyIncome = annonce.getRpcCalcul().getRevenusFortuneImmobiliere();
        mortgageInterest = annonce.getRpcCalcul().getInteretsHypothequaires();
        maintenanceFees = annonce.getRpcCalcul().getFraisEntretien();
        interestFeesEligible = annonce.getRpcCalcul().getInteretsHypothequairesFraisMaintenance();
    }

    public Montant getRealProperty() {
        return realProperty;
    }

    public Montant getMortgageDebts() {
        return mortgageDebts;
    }

    public Montant getPropertyIncome() {
        return propertyIncome;
    }

    public Montant getMortgageInterest() {
        return mortgageInterest;
    }

    public Montant getMaintenanceFees() {
        return maintenanceFees;
    }

    public Montant getInterestFeesEligible() {
        return interestFeesEligible;
    }

}
