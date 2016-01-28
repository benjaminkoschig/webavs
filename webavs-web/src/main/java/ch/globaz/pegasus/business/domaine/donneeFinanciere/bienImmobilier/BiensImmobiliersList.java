package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public interface BiensImmobiliersList {
    public Montant sumMontantLoyerEncaisse();

    public Montant sumMontantValeurLocative();

    public Montant sumMontantValeurLocative(ProprieteType type);

    public Montant sumInteretHypothecaire();

    public Montant sumMontantValeurLocativePartPropriete();

    public Montant sumMontantValeurLocativePartPropriete(ProprieteType type);

    public Montant sumSousLocation();
}
