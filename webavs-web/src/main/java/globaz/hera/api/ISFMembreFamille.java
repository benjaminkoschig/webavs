/*
 * mmu Créé le 14 oct. 05
 */
package globaz.hera.api;

/**
 * DOCUMENT ME!
 * 
 * @author mmu
 * 
 *         <p>
 *         14 oct. 05
 *         </p>
 */
public interface ISFMembreFamille {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut canton
     * 
     * @return la valeur courante de l'attribut canton
     */
    public String getCsCantonDomicile();

    /**
     * Retourne le code du pays de domicile. P. Ex. 100 pour la Suisse
     * 
     * @return le code du pays de domicile. P. Ex. 100 pour la Suisse
     */
    public String getPays();

    public String getCsDomaine();

    /**
     * getter pour l'attribut etat civil
     * 
     * est un code system de ISFSituationFamiliale.CS_ETAT_CIVIL_...
     * 
     * @return l'état civil à la date donnée; si aucune date fournie, le dernier état civil est renvoyé. L'état civil
     *         d'un requérant est toujours renseigné alors que celle d'un membre non requérant peut être nulle s'il n'a
     *         aucune relation à la date donnée.
     */
    public String getCsEtatCivil();

    /**
     * getter pour l'attribut nationalite
     * 
     * @return la valeur courante de l'attribut nationalite
     */
    public String getCsNationalite();

    /**
     * getter pour l'attribut sexe revoie un code des tiers ITIPersonne.CS_HOMME/FEMME
     * 
     * @return la valeur courante de l'attribut sexe
     */
    public String getCsSexe();

    /**
     * getter pour l'attribut date deces
     * 
     * @return la valeur courante de l'attribut date deces
     */
    public String getDateDeces();

    /**
     * getter pour l'attribut date naissance
     * 
     * @return la valeur courante de l'attribut date naissance
     */
    public String getDateNaissance();

    /**
     * getter pour l'attribut idTiers revoie l'id du tiers
     * 
     * @return la valeur courante de l'attribut idTiers
     */
    public String getIdTiers();

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom();

    /**
     * getter pour l'attribut no avs
     * 
     * @return la valeur courante de l'attribut no avs
     */
    public String getNss();

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom();

    public String getRelationAuLiant();

}
