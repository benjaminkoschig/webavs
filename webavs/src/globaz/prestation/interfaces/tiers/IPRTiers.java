/*
 * Créé le 25 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.interfaces.tiers;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public interface IPRTiers {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String FIELD_TI_IDTIERS = "HTITIE";
    public static final String FIELD_NUMERO_AVS = "HXNAVS";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HIST = "TIHAVSP";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateDeces();

    /**
     * getter pour l'attribut date naissance
     * 
     * @return la valeur courante de l'attribut date naissance
     */
    public String getDateNaissance();

    /**
     * getter pour l'attribut id canton
     * 
     * @return la valeur courante de l'attribut id canton
     */
    public String getIdCanton();

    /**
     * getter pour l'attribut id pays
     * 
     * @return la valeur courante de l'attribut id pays
     */
    public String getIdPays();

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers();

    /**
     * getter pour l'attribut id langue
     * 
     * @return la valeur courante de l'attribut id langue
     */
    public String getLangue();

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS();

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom();

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom();

    /**
     * getter pour l'attribut sexe
     * 
     * @return la valeur courante de l'attribut sexe
     */
    public String getSexe();
}
