/*
 * mmu Cr�� le 17 oct. 05
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
public interface ISFEnfant {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date adoption
     * 
     * @return la valeur courante de l'attribut date adoption
     */
    public String getDateAdoption();

    /**
     * getter pour l'attribut no avs mere Le conjoint inconnu peut �tre retourn�
     * 
     * @return la valeur courante de l'attribut no avs mere ou null en cas d'erreur ou de valeur ind�finie
     */
    public String getNoAvsMere();

    /**
     * getter pour l'attribut no avs pere Le conjoint inconnu peut �tre retourn�
     * 
     * @return la valeur courante de l'attribut no avs pere ou null en cas d'erreur ou de valeur ind�finie
     */
    public String getNoAvsPere();

    /**
     * getter pour l'attribut nom mere
     * 
     * @return la valeur courante de l'attribut nom mere
     */
    public String getNomMere();

    /**
     * getter pour l'attribut nom pere
     * 
     * @return la valeur courante de l'attribut nom pere
     */
    public String getNomPere();

    /**
     * getter pour l'attribut no avs
     * 
     * @return le num�ro AVS de l'enfant ou null si ind�termin�
     */
    public String getNss();

    /**
     * getter pour l'attribut prenom mere
     * 
     * @return la valeur courante de l'attribut prenom mere
     */
    public String getPrenomMere();

    /**
     * getter pour l'attribut prenom pere
     * 
     * @return la valeur courante de l'attribut prenom pere
     */
    public String getPrenomPere();

    /**
     * retourne true si l'enfant est recueilli pour l'attribut recueilli
     * 
     * @return la valeur courante de l'attribut recueilli
     */
    public boolean isRecueilli();
}
