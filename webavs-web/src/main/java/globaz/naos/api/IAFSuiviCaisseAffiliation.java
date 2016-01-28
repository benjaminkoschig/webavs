package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des suivi d'assurances.
 * 
 * @author David Girardin
 */
public interface IAFSuiviCaisseAffiliation extends BIEntity {

    /**
     * Cl� de recherche par Affiliation Id.
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Cl� de recherche par genre d'assurance.
     */
    public static final String FIND_FOR_GENRE_CAISSE = "setForGenreCaisse";

    /**
     * Genre suivi AF
     */
    public static final String GENRE_CAISSE_AF = "830002";

    /**
     * Genre suivi AVS
     */
    public static final String GENRE_CAISSE_AVS = "830001";

    /**
     * Genre suivi LAA
     */
    public static final String GENRE_CAISSE_LAA = "830004";

    /**
     * Genre suivi LPP
     */
    public static final String GENRE_CAISSE_LPP = "830003";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant les suivis.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_GENRE_CAISSE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max. sont retourn�s.<br>
     * <br>
     * 
     * @return une liste des suivis trouv�s
     * @param params
     *            crit�res de recherche
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau de suivis.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_GENRE_CAISSE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retourn�s.<br>
     * <br>
     * <br>
     * 
     * @return une liste des suivis trouv�s
     * @param params
     *            crit�res de recherche
     * @exception Exception
     *                si echec
     */
    public IAFSuiviCaisseAffiliation[] findSuiviCaisse(Hashtable params) throws Exception;

    /**
     * Renvoie l'id de l'affiliation.
     * 
     * @return id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Renvoie la cat�gorie de salari�.
     * 
     * @return la cat�gorie de salari�
     */
    public java.lang.String getCategorieSalarie();

    /**
     * Renvoie la date de d�but du suivi.
     * 
     * @return la date de d�but du suivi au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin du suivi.
     * 
     * @return la date de fin du suivi au format jj.mm.aaaa ou vide si ind�fini
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie le genre de l'assurance du suivi. Se r�f�rer aux constantes {@link #GENRE_CAISSE_AVS genres caisse}
     * 
     * @return le genre de l'assurance du suivi
     */
    public java.lang.String getGenreCaisse();

    /**
     * Renvoie l'id du tiers repr�sentant la caisse concern�e.
     * 
     * @return id du tiers repr�sentant la caisse concern�e
     */
    public java.lang.String getIdTiersCaisse();

    /**
     * Renvoie le motif si non soumis.
     * 
     * @return le motif si non soumis
     */
    public java.lang.String getMotif();

    /**
     * Renvoie le num�ro d'affiliation de la caisse externe.
     * 
     * @return le num�ro d'affiliation de la caisse externe
     */
    public java.lang.String getNumeroAffileCaisse();

    /**
     * Renvoie l'id du suivi.
     * 
     * @return id du suivi
     */
    public java.lang.String getSuiviCaisseId();

    /**
     * Renvoie true si l'attestion IP a �t� re�ue.
     * 
     * @return true si l'attestion IP a �t� re�ue
     */
    public java.lang.Boolean isAttestationIp();

    /**
     * R�cup�re l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @throws Exception
     *             si la r�cuperation a �chou�
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * D�finit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * D�finit si l'attestion IP a �t� re�ue ou non.
     * 
     * @param newAttestationIp
     *            true si l'attestion IP a �t� re�ue, false sinon
     */
    public void setAttestationIp(java.lang.Boolean newAttestationIp);

    /**
     * D�finit la cat�gorie de salari�.
     * 
     * @param newCategorieSalarie
     *            la cat�gorie de salari�
     */
    public void setCategorieSalarie(java.lang.String newCategorieSalarie);

    /**
     * D�finit la date de d�but du suivi.
     * 
     * @param newDateDebut
     *            la date de d�but du suivi avec le format jj.mm.aaaa
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin du suivi.
     * 
     * @param newDateFin
     *            la date de fin du suivi avec le format jj.mm.aaaa
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * D�finit le genre de l'assurance du suivi.
     * 
     * @param newGenreCaissse
     *            l'id du tiers repr�sentant la caisse
     */
    public void setGenreCaisse(java.lang.String newGenreCaissse);

    /**
     * D�finit l'id du tiers repr�sentant la caisse.
     * 
     * @param newIdTiersCaissse
     *            l'id du tiers repr�sentant la caisse
     */
    public void setIdTiersCaisse(java.lang.String newIdTiersCaissse);

    /**
     * D�finit le motif si non soumis.
     * 
     * @param newMotif
     *            le motif si non soumis
     */
    public void setMotif(java.lang.String newMotif);

    /**
     * D�finit le num�ro d'affiliation de la caisse externe.
     * 
     * @param newNumeroAffilieCaisse
     *            le num�ro d'affiliation de la caisse externe
     */
    public void setNumeroAffileCaisse(java.lang.String newNumeroAffilieCaisse);

    /**
     * D�finit l'id du suivi.
     * 
     * @param newSuiviCaisseId
     *            l'id du suivi
     */
    public void setSuiviCaisseId(java.lang.String newSuiviCaisseId);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction � utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws Exception;
}
