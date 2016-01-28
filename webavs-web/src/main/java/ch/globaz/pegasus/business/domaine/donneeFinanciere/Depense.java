package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Depense {

    /**
     * Calcul les dépenses en prenant en compte les informations nécessaires.
     * 
     * @return
     */
    public Montant computeDepense();

}
