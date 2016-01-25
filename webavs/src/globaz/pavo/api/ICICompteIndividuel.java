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
    /** CI au registre des assur�s */
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
     * Renvoie l'ann�e d'ouverture du CI au format <tt>AAAA</tt>.
     * 
     * @return l'ann�e d'ouverture du CI.
     */
    public String getAnneeOuverture();

    /**
     * Renvoie l'id interne du CI.
     * 
     * @return l'id interne du CI.
     */
    public String getCompteIndividuelId();

    /**
     * Renvoie la date de la derni�re cl�ture au format <tt>MMAA</tt>.
     * 
     * @return la date de la derni�re cl�ture.
     */
    public String getDateClotureMMAA();

    /**
     * Renvoie la date de cr�ation du CI � la caisse dans le format <tt>jj.mm.aaaa</tt>.
     * 
     * @return la date de cr�ation du CI � la caisse.
     */
    public String getDateCreation();

    /**
     * Renvoie la date de naissance de l'assur� dans le format <tt>jj.mm.aaaa</tt>.
     * 
     * @return la date de naissance de l'assur�.
     */
    public String getDateNaissance();

    /**
     * Renvoie le derni�re motif des annonces de ce CI.
     * 
     * @return le derni�re motif.
     */
    public String getDernierMotifArc();

    /**
     * Renvoie le nom de l'assur�.
     * 
     * @return le nom de l'assur�.
     */
    public String getNomPrenom();

    /**
     * Retourne le no et le nom du dernier employeur dans le format <tt>no_affili� nom_affili�</tt>. Date de cr�ation :
     * (15.01.2003 09:41:26)
     * 
     * @return le no et le nom du dernier employeur.
     */
    public String getNoNomDernierEmployeur();

    /**
     * Renvoie le num�ro de l'assur� sans points de s�parations.
     * 
     * @return le num�ro de l'assur�.
     */
    public String getNumeroAvs();

    /**
     * Renvoie la r�f�rence interne du CI.
     * 
     * @return la r�f�rence interne du CI.
     */
    public String getReferenceInterne();

    /**
     * Renvoie le registre qui peut �tre du type suivant:
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
     * Renvoie le sexe de l'assur� qui peut �tre du type suivant:
     * <ul>
     * <li><tt>CS_HOMME</tt></li>
     * <li><tt>CS_FEMME</tt></li>
     * </ul>
     * 
     * @return le sexe de l'assur�.
     */
    public String getSexe();

    /**
     * � venir...
     */
    public boolean isCiOuvert();

    /**
     * Renvoie le CI de l'assur� en fonction du num�ro.<br>
     * L'appel de la m�thode <tt>isNew()</tt> � la suite de cette derni�re permet de v�rifier si le CI existe � la
     * caisse.
     * 
     * @param noAVS
     *            le num�ro de l'assur� dans le format avec ou sans points de s�parations.
     * @param transaction
     *            la transaction � utiliser pour faire la requ�te.
     * @exception java.lang.Exception
     *                si le chargement a �chou�.
     */
    public void load(String noAVS, BITransaction transaction) throws java.lang.Exception;

    void setMethodsToLoad(String[] methodsName);

}