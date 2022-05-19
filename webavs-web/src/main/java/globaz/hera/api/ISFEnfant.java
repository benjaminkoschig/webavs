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
public interface ISFEnfant {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date adoption
     * 
     * @return la valeur courante de l'attribut date adoption
     */
    String getDateAdoption();

    /**
     * getter pour l'attribut no avs mere Le conjoint inconnu peut être retourné
     * 
     * @return la valeur courante de l'attribut no avs mere ou null en cas d'erreur ou de valeur indéfinie
     */
    String getNoAvsParent2();

    /**
     * getter pour l'attribut no avs pere Le conjoint inconnu peut être retourné
     * 
     * @return la valeur courante de l'attribut no avs pere ou null en cas d'erreur ou de valeur indéfinie
     */
    String getNoAvsParent1();

    /**
     * getter pour l'attribut nom mere
     * 
     * @return la valeur courante de l'attribut nom mere
     */
    String getNomParent2();

    /**
     * getter pour l'attribut nom pere
     * 
     * @return la valeur courante de l'attribut nom pere
     */
    String getNomParent1();

    /**
     * getter pour l'attribut no avs
     * 
     * @return le numéro AVS de l'enfant ou null si indéterminé
     */
    String getNss();

    /**
     * getter pour l'attribut prenom mere
     * 
     * @return la valeur courante de l'attribut prenom mere
     */
    String getPrenomParent2();

    /**
     * getter pour l'attribut prenom pere
     * 
     * @return la valeur courante de l'attribut prenom pere
     */
    String getPrenomParent1();

    /**
     * retourne true si l'enfant est recueilli pour l'attribut recueilli
     * 
     * @return la valeur courante de l'attribut recueilli
     */
    boolean isRecueilli();

    /**
     * getter pour l'attribut date de naissance de l'enfant
     *
      * @return la valeur courante de l'attribut date de naissance
     */
    String getDateNaissance();

    /**
     * getter pour l'attribut date de naissance du pere
     *
     * @return la valeur courante de l'attribut date de naissance pere
     */
    String getDateNaissanceParent1();

    /**
     * getter pour l'attribut date de naissance de la mere
     *
     * @return la valeur courante de l'attribut date de naissance mere
     */
    String getDateNaissanceParent2();

    /**
     * @return le CS du Sexe du parent 1.
     */
    String getSexeParent1();

    /**
     * @return le CS du Sexe du parent 2.
     */
    String getSexeParent2();
}
