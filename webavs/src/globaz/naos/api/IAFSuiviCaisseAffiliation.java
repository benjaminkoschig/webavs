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
     * Clé de recherche par Affiliation Id.
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";

    /**
     * Clé de recherche par genre d'assurance.
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
     *            la transaction à utiliser ou null si non transactionnel
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant les suivis.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_GENRE_CAISSE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max. sont retournés.<br>
     * <br>
     * 
     * @return une liste des suivis trouvés
     * @param params
     *            critères de recherche
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau de suivis.<br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_GENRE_CAISSE</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retournés.<br>
     * <br>
     * <br>
     * 
     * @return une liste des suivis trouvés
     * @param params
     *            critères de recherche
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
     * Renvoie la catégorie de salarié.
     * 
     * @return la catégorie de salarié
     */
    public java.lang.String getCategorieSalarie();

    /**
     * Renvoie la date de début du suivi.
     * 
     * @return la date de début du suivi au format jj.mm.aaaa ou vide si indéfini
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin du suivi.
     * 
     * @return la date de fin du suivi au format jj.mm.aaaa ou vide si indéfini
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie le genre de l'assurance du suivi. Se référer aux constantes {@link #GENRE_CAISSE_AVS genres caisse}
     * 
     * @return le genre de l'assurance du suivi
     */
    public java.lang.String getGenreCaisse();

    /**
     * Renvoie l'id du tiers représentant la caisse concernée.
     * 
     * @return id du tiers représentant la caisse concernée
     */
    public java.lang.String getIdTiersCaisse();

    /**
     * Renvoie le motif si non soumis.
     * 
     * @return le motif si non soumis
     */
    public java.lang.String getMotif();

    /**
     * Renvoie le numéro d'affiliation de la caisse externe.
     * 
     * @return le numéro d'affiliation de la caisse externe
     */
    public java.lang.String getNumeroAffileCaisse();

    /**
     * Renvoie l'id du suivi.
     * 
     * @return id du suivi
     */
    public java.lang.String getSuiviCaisseId();

    /**
     * Renvoie true si l'attestion IP a été reçue.
     * 
     * @return true si l'attestion IP a été reçue
     */
    public java.lang.Boolean isAttestationIp();

    /**
     * Récupère l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @throws Exception
     *             si la récuperation a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            l'id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit si l'attestion IP a été reçue ou non.
     * 
     * @param newAttestationIp
     *            true si l'attestion IP a été reçue, false sinon
     */
    public void setAttestationIp(java.lang.Boolean newAttestationIp);

    /**
     * Définit la catégorie de salarié.
     * 
     * @param newCategorieSalarie
     *            la catégorie de salarié
     */
    public void setCategorieSalarie(java.lang.String newCategorieSalarie);

    /**
     * Définit la date de début du suivi.
     * 
     * @param newDateDebut
     *            la date de début du suivi avec le format jj.mm.aaaa
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin du suivi.
     * 
     * @param newDateFin
     *            la date de fin du suivi avec le format jj.mm.aaaa
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit le genre de l'assurance du suivi.
     * 
     * @param newGenreCaissse
     *            l'id du tiers représentant la caisse
     */
    public void setGenreCaisse(java.lang.String newGenreCaissse);

    /**
     * Définit l'id du tiers représentant la caisse.
     * 
     * @param newIdTiersCaissse
     *            l'id du tiers représentant la caisse
     */
    public void setIdTiersCaisse(java.lang.String newIdTiersCaissse);

    /**
     * Définit le motif si non soumis.
     * 
     * @param newMotif
     *            le motif si non soumis
     */
    public void setMotif(java.lang.String newMotif);

    /**
     * Définit le numéro d'affiliation de la caisse externe.
     * 
     * @param newNumeroAffilieCaisse
     *            le numéro d'affiliation de la caisse externe
     */
    public void setNumeroAffileCaisse(java.lang.String newNumeroAffilieCaisse);

    /**
     * Définit l'id du suivi.
     * 
     * @param newSuiviCaisseId
     *            l'id du suivi
     */
    public void setSuiviCaisseId(java.lang.String newSuiviCaisseId);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction à utiliser ou null si non transactionnel
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws Exception;
}
