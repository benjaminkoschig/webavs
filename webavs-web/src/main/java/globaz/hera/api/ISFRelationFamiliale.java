/*
 * mmu Créé le 17 oct. 05
 */
package globaz.hera.api;

/**
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         17 oct. 05
 *         </p>
 */
public interface ISFRelationFamiliale {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut();

    public String getDateDebutRelation();

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin();

    /**
     * getter pour l'attribut nom femme
     * 
     * @return la valeur courante de l'attribut idMembreFamille de la femme
     */
    public String getIdMembreFamilleFemme();

    /**
     * getter pour l'attribut idMembreFamille de l'home
     * 
     * @return la valeur courante de l'attribut idMembreFamille homme
     */
    public String getIdMembreFamilleHomme();

    /**
     * getter pour l'attribut no avs femme
     * 
     * @return la valeur courante de l'attribut no avs femme
     */
    public String getNoAvsFemme();

    /**
     * getter pour l'attribut no avs homme
     * 
     * @return la valeur courante de l'attribut no avs homme
     */
    public String getNoAvsHomme();

    /**
     * getter pour l'attribut nom femme
     * 
     * @return la valeur courante de l'attribut nom femme
     */
    public String getNomFemme();

    /**
     * getter pour l'attribut nom homme
     * 
     * @return la valeur courante de l'attribut nom homme
     */
    public String getNomHomme();

    /**
     * getter pour l'attribut prénom Femme
     * 
     * @return la valeur courante de l'attribut prénom Femme
     */
    public String getPrenomFemme();

    /**
     * getter pour l'attribut prénom Homme
     * 
     * @return la valeur courante de l'attribut prénom Homme
     */
    public String getPrenomHomme();

    /**
     * getter pour l'attribut type lien
     * 
     * <p>
     * Code system de la classe ISFSituationFamilila.CS_TYPE_LIEN_... Pour être veuf les conjoints doivent être mariés
     * lors du décès d'un conjoint, s'ils sont séparés ou divorcés, il n'y a pas de veuvage.
     * </p>
     * 
     * @return la valeur courante de l'attribut type lien
     */
    public String getTypeLien();

    public String getTypeRelation();

}
