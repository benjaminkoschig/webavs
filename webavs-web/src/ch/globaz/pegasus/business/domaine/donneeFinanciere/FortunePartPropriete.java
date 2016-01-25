package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface FortunePartPropriete extends Fortune {

    /**
     * Compute fortune en prenant en compte la part pour tous les types de propriété
     * 
     * @return
     */
    public Montant computeFortunePartPropriete();

}
