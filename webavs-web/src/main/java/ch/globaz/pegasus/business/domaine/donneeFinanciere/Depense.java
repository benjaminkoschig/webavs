package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Depense {

    /**
     * Calcul les d�penses en prenant en compte les informations n�cessaires.
     * 
     * @return
     */
    public Montant computeDepense();

}
