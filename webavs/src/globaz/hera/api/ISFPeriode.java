/*
 * mmu Créé le 17 oct. 05
 */
package globaz.hera.api;

import globaz.hera.enums.TypeDeDetenteur;

/**
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         17 oct. 05
 *         </p>
 */
public interface ISFPeriode {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut de la période
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut();

    /**
     * getter pour l'attribut date fin de la période
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin();

    /**
     * getter pour l'attribut idMembreFamille du detenteur d'une période BTE
     * 
     * @return la valeur courante de l'attribut id membre famille detenteur ou 0 si la période n'est pas de type BTE
     */
    public String getIdDetenteurBTE();

    /**
     * @return le numéro avs du membre de famille auquel appartient la période ou null en cas d'erreur
     */
    public String getNoAvs();

    /**
     * getter pour l'attribut NoAvs du detenteur d'une période de type BTE
     * 
     * @return la valeur courante de l'attribut no avs detenteur ou null si la période n'est pas de type BTE
     */
    public String getNoAvsDetenteurBTE();

    /**
     * getter pour l'attribut pays
     * 
     * @return la valeur courante de l'attribut pays quand le type est de type Assurance Etrangère ou 0 pour les autres
     *         types de période
     */
    public String getPays();

    /**
     * getter pour l'attribut type de la période
     * 
     * @return la valeur courante de l'attribut type periode n'est pas Assurance Etrangère
     */
    public String getType();

    /**
     * Retourne le code système du type de détenteur
     * 
     * @see TypeDeDetenteur
     * @return le code système du type de détenteur
     */
    public String getCsTypeDeDetenteur();
}
