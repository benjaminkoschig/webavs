package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Revenu {
    /**
     * Calcul le montant du revenu annuel en fonction des différentes informations à prendre en compte.
     * 
     * @return Montant
     */
    public abstract Montant computeRevenuAnnuel();

    /**
     * Retourne le montant brut de la données financière.
     * 
     * @return
     */
    public abstract Montant computeRevenuAnnuelBrut();
}
