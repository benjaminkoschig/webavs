package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceHousingOwner {

    protected Montant selfInhabitedProperty;
    protected Montant selfInhabitedPropertyDeductible;
    protected Montant rentalValue;

    public AnnonceHousingOwner(RpcDecisionAnnonceComplete annonce) {
        selfInhabitedProperty = annonce.getRpcCalcul().getValeurImmeubleHabitation();
        selfInhabitedPropertyDeductible = annonce.getRpcCalcul().getFranchiseImmeubleHabitation();
        rentalValue = annonce.getPersonsElementsCalcul().sumValeurLocativeProprietaire();
        // rentalValue = annonce.getRpcCalcul().getRevenuValeurLocativeAppHabitePrincipale();
    }

    public Montant getSelfInhabitedProperty() {
        return selfInhabitedProperty;
    }

    public Montant getSelfInhabitedPropertyDeductible() {
        return selfInhabitedPropertyDeductible;
    }

    public Montant getRentalValue() {
        return rentalValue;
    }

}
