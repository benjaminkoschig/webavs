/*
 * mmu Cr�� le 17 oct. 05
 */
package globaz.hera.api;

import globaz.hera.enums.TypeDeDetenteur;

/**
 * DOCUMENT ME!
 *
 * @author mmu
 *
 * <p>
 * 17 oct. 05
 * </p>
 */
public interface ISFPeriode {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut de la p�riode
     *
     * @return la valeur courante de l'attribut date debut
     */
    String getDateDebut();

    /**
     * getter pour l'attribut date fin de la p�riode
     *
     * @return la valeur courante de l'attribut date fin
     */
    String getDateFin();

    /**
     * getter pour l'attribut idMembreFamille du detenteur d'une p�riode BTE
     *
     * @return la valeur courante de l'attribut id membre famille detenteur ou 0 si la p�riode n'est pas de type BTE
     */
    String getIdDetenteurBTE();

    /**
     * @return le num�ro avs du membre de famille auquel appartient la p�riode ou null en cas d'erreur
     */
    String getNoAvs();

    /**
     * getter pour l'attribut NoAvs du detenteur d'une p�riode de type BTE
     *
     * @return la valeur courante de l'attribut no avs detenteur ou null si la p�riode n'est pas de type BTE
     */
    String getNoAvsDetenteurBTE();

    /**
     * getter pour l'attribut pays
     *
     * @return la valeur courante de l'attribut pays quand le type est de type Assurance Etrang�re ou 0 pour les autres
     * types de p�riode
     */
    String getPays();

    /**
     * getter pour l'attribut type de la p�riode
     *
     * @return la valeur courante de l'attribut type periode n'est pas Assurance Etrang�re
     */
    String getType();

    /**
     * Retourne le code syst�me du type de d�tenteur
     *
     * @return le code syst�me du type de d�tenteur
     * @see TypeDeDetenteur
     */
    String getCsTypeDeDetenteur();

    /**
     * @return l'Id du recueillant
     */
    String getNoAvsRecueillant();
}
