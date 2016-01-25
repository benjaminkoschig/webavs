package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des comptes individuels.
 * 
 * @author David Girardin
 */
public interface ICICompteIndividuel extends BIEntity {
    /** Femme */
    public final static String CS_FEMME = "316001";
    /** Homme */
    public final static String CS_HOMME = "316000";
    /** CI au registre des assurés */
    public final static String CS_REGISTRE_ASSURES = "309001";
    /** CI genre 6 */
    public final static String CS_REGISTRE_GENRES_6 = "309002";
    /** CI genre 7 */
    public final static String CS_REGISTRE_GENRES_7 = "309003";
    /** CI historique */
    public final static String CS_REGISTRE_HISTORIQUE = "309005";
    /** CI provisoire */
    public final static String CS_REGISTRE_PROVISOIRE = "309004";
    /** numeroAVS */
    public final static String FIND_FOR_NUM_AVS_LIKE = "setLikeNumeroAvs";

    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie l'année d'ouverture du CI au format <tt>AAAA</tt>.
     * 
     * @return l'année d'ouverture du CI.
     */
    public String getAnneeOuverture();

    /**
     * Renvoie l'id interne du CI.
     * 
     * @return l'id interne du CI.
     */
    public String getCompteIndividuelId();

    /**
     * Renvoie la date de la dernière clôture au format <tt>MMAA</tt>.
     * 
     * @return la date de la dernière clôture.
     */
    public String getDateClotureMMAA();

    /**
     * Renvoie la date de création du CI à la caisse dans le format <tt>jj.mm.aaaa</tt>.
     * 
     * @return la date de création du CI à la caisse.
     */
    public String getDateCreation();

    /**
     * Renvoie la date de naissance de l'assuré dans le format <tt>jj.mm.aaaa</tt>.
     * 
     * @return la date de naissance de l'assuré.
     */
    public String getDateNaissance();

    /**
     * Renvoie le dernière motif des annonces de ce CI.
     * 
     * @return le dernière motif.
     */
    public String getDernierMotifArc();

    /**
     * Renvoie le nom de l'assuré.
     * 
     * @return le nom de l'assuré.
     */
    public String getNomPrenom();

    /**
     * Retourne le no et le nom du dernier employeur dans le format <tt>no_affilié nom_affilié</tt>. Date de création :
     * (15.01.2003 09:41:26)
     * 
     * @return le no et le nom du dernier employeur.
     */
    public String getNoNomDernierEmployeur();

    /**
     * Renvoie le numéro de l'assuré sans points de séparations.
     * 
     * @return le numéro de l'assuré.
     */
    public String getNumeroAvs();

    /**
     * Renvoie la référence interne du CI.
     * 
     * @return la référence interne du CI.
     */
    public String getReferenceInterne();

    /**
     * Renvoie le registre qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_REGISTRE_ASSURES</tt></li>
     * <li><tt>CS_REGISTRE_GENRES_6</tt></li>
     * <li><tt>CS_REGISTRE_GENRES_7</tt></li>
     * <li><tt>CS_REGISTRE_PROVISOIRE</tt></li>
     * <li><tt>CS_REGISTRE_HISTORIQUE</tt></li>
     * </ul>
     * 
     * @return le registre du CI.
     */
    public String getRegistre();

    /**
     * Renvoie le sexe de l'assuré qui peut être du type suivant:
     * <ul>
     * <li><tt>CS_HOMME</tt></li>
     * <li><tt>CS_FEMME</tt></li>
     * </ul>
     * 
     * @return le sexe de l'assuré.
     */
    public String getSexe();

    /**
     * à venir...
     */
    public boolean isCiOuvert();

    /**
     * Renvoie le CI de l'assuré en fonction du numéro.<br>
     * L'appel de la méthode <tt>isNew()</tt> à la suite de cette dernière permet de vérifier si le CI existe à la
     * caisse.
     * 
     * @param noAVS
     *            le numéro de l'assuré dans le format avec ou sans points de séparations.
     * @param transaction
     *            la transaction à utiliser pour faire la requête.
     * @exception java.lang.Exception
     *                si le chargement a échoué.
     */
    public void load(String noAVS, BITransaction transaction) throws java.lang.Exception;

    void setMethodsToLoad(String[] methodsName);

}