package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Fortune {

    /**
     * Calcul fortune en prenant en compte la part pour tous les types de propriété sauf "Nu propriétaire".
     * 
     * @return Montant
     */
    public Montant computeFortune();

    /**
     * Retourne le montant brut.
     * 
     * @return Montant
     */
    public Montant computeFortuneBrut();
}
