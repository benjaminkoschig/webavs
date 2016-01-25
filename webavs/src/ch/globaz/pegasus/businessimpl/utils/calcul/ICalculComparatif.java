package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.util.Collection;

public interface ICalculComparatif {

    /**
     * Compare la liste d'id des personnes du plan de calcul avec la liste en paramètre
     * 
     * @param idPersonnes
     *            liste d'id de personnes à comparer. La liste n'a pas besoin d'être triée
     * @return vrai si les id sont identique
     */
    public boolean equalsPersonnes(Collection<String> idPersonnes);

    /**
     * Retourne l'etat de la PC
     * 
     * @return l'etat de la Pc
     */
    public String getEtatPC();

    /**
     * Retourne l'excedent de fortune annuel
     * 
     * @return
     */
    public abstract String getExcedentAnnuel();

    /**
     * Retourne une liste d'id droit membre famille des personnes faisant partie du plan de calcul
     * 
     * @return liste ordonnée d'id des personnes
     */
    public Collection<String> getIdPersonnes();

    /**
     * Retourne l'id du plan de calcul en BD représentant ce calcul comparatif
     * 
     * @return id du plan de calcul
     */
    public abstract String getIdPlanCalcul();

    /**
     * Retourne le montant des donnation ou dessaisisements
     * 
     * @return le montant des donnations
     */
    public abstract String getMontantDonation();

    /**
     * Retourne le montant mensuel octroyé par la pc
     * 
     * @return le montant mensuel
     */
    public abstract String getMontantPCMensuel();

    /**
     * Retourne le montant du prix des homes
     * 
     * @return le montant des forfaits de homes
     */
    public abstract String getMontantPrixHome();

    /**
     * Retourne le montant des rentes AVS AI
     * 
     * @return le montant des rentes avs ai
     */
    public abstract String getMontantRentesAvsAi();

    /**
     * Retourne la racine de l'arborescence des résultats du calcul comparatif
     * 
     * @return le tuple racine des résultats
     */
    public abstract TupleDonneeRapport getMontants();

    /**
     * Retourne le nombre d'enfants
     * 
     * @return le nombre d'enfants
     */
    public abstract String getNbEnfants();

}
