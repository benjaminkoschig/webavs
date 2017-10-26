package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Revenu {
    /**
     * Calcul le montant du revenu annuel en fonction des diff�rentes informations � prendre en compte.
     * 
     * @return Montant
     */
    public abstract Montant computeRevenuAnnuel();

    /**
     * Retourne le montant brut de la donn�es financi�re.
     * 
     * @return
     */
    public abstract Montant computeRevenuAnnuelBrut();
}
