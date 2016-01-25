package globaz.hera.api;

/**
 * <p>
 * Description d'un membre de famille. <br/>
 * Cette interface est uniquement utilisée comme valeur de retour des méthodes de recherches
 * </p>
 * <p>
 * Utilisation de l'interface :<br/>
 * <code>	
 * BIApplication application = GlobazSystem.getApplication("HERA_REMOTE");<br/>
 * BSession session = (BSession) application.newSession("globazf", "ssiiadm");<br/>
 * ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD);<br/>
 * </code>
 * </p>
 * <p>
 * A partir d'une instance de ISFSituationFamiliale en fonction du domaine, toutes les méthodes concernant la situation
 * familiale peuvent être appelée<br/>
 * <code>
 * ISFMembreFamilleRequerant[] result = sf.getMembresFamille("257667","12.12.1998");<br/>
 * </code>
 * 
 * @author MMU
 * @since 13 octobre 2005 (Date de création)
 */
public interface ISFMembreFamilleRequerant {

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

    /**
     * getter pour l'attribut canton
     * 
     * @return la valeur courante de l'attribut canton
     */
    public String getCsEtatCivil();

    /**
     * getter pour l'attribut nationalite
     * 
     * @return la valeur courante de l'attribut nationalite
     */
    public String getCsNationalite();

    /**
     * getter pour l'attribut sexe
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
     * getter pour l'attribut id membre famille
     * 
     * @return la valeur courante de l'attribut id membre famille
     */
    public String getIdMembreFamille();

    /**
     * getter pour l'attribut idTiers
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

    /**
     * getter pour l'attribut relation au requerant
     * 
     * @return un code system indiquant si la relation est de type conjoint ou enfant ou le requerant lui-meme
     *         ISFSituationFamiliale.CS_TYPE_RELATION_...
     */
    public String getRelationAuRequerant();
}
